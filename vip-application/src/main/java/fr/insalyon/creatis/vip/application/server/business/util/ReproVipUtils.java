package fr.insalyon.creatis.vip.application.server.business.util;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.web.bindery.requestfactory.server.Pair;

import fr.insalyon.creatis.vip.core.server.business.BusinessException;
import fr.insalyon.creatis.vip.datamanager.client.bean.ExternalPlatform;
import fr.insalyon.creatis.vip.datamanager.server.business.ExternalPlatformBusiness;


public class ReproVipUtils {

    private ExternalPlatformBusiness externalPlatformBusiness;
    private Map<String, String> providerInformations;
    private Map<String, List<String>> simplifiedInputs;

    enum ProviderType {
        GIRDER,
        LOCAL
    }

    public ReproVipUtils(ExternalPlatformBusiness externalPlatformBusiness, String vipURL) {
        this.externalPlatformBusiness = externalPlatformBusiness;
        this.providerInformations = new LinkedHashMap<>();
        this.simplifiedInputs = new LinkedHashMap<>();

        this.providerInformations.put("vip_url", vipURL);
    }

    public void parse(Map<String, String> inputs) throws BusinessException {
        Map<String, List<String>> expandedInputs = getExpandedInputs(inputs);

        Pair<ProviderType, Map<String, List<String>>> type = detectType(expandedInputs);
        
        providerInformations.put("storage_type", type.getA().toString());
        switch (type.getA()) {
            case GIRDER:
                providerInformations.putAll(getGirderInformations(type.getB()));
                simplifiedInputs = getGirderInputs(providerInformations.get("storage_id"), expandedInputs, type.getB().keySet());
                break;
            case LOCAL:
                providerInformations.putAll(getLocalInformations(type.getB()));
                simplifiedInputs = expandedInputs;
                break;
        }
    }

    public Map<String, String> getProviderInformations() {
        return providerInformations;
    }

    public Map<String, List<String>> getSimplifiedInputs() {
        return simplifiedInputs;
    }

    private Map<String, List<String>> getExpandedInputs(Map<String, String> inputs) {
        inputs.remove("results-directory");

        return inputs.entrySet().stream()
            .collect(Collectors.toMap(
                Map.Entry::getKey, e -> Arrays.asList(e.getValue().split("; "))));
    }

    private Pair<ProviderType, Map<String, List<String>>> detectType(Map<String, List<String>> inputs) {
        ProviderType providerType = ProviderType.LOCAL;
        Map<String, List<String>> concernedKeys = new HashMap<>();

        for (var entry : inputs.entrySet()) {
            for (String item : entry.getValue()) {
                if (item.startsWith("girder:/")) {
                    concernedKeys.put(entry.getKey(), entry.getValue());
                    providerType = ProviderType.GIRDER;
                }
            }
        }
        return new Pair<ProviderType, Map<String, List<String>>>(providerType, concernedKeys);
    }

    private Map<String, String> getLocalInformations(Map<String, List<String>> filteredInputs) {
        return Collections.emptyMap();
    }

    private Map<String, String> getGirderInformations(Map<String, List<String>> filteredInputs) throws BusinessException {
        Map<String, String> result = new HashMap<>();
        List<String> stringUris = filteredInputs.values().stream()
            .flatMap(Collection::stream).collect(Collectors.toList());
        List<Map<String, String>> uris = new ArrayList<>();

        for (String uri : stringUris) {
            uris.add(transformURItoMap(uri));
        }
        
        result.put("storage_url", getGirderURL(uris.getFirst()));
        result.put("storage_id", getGirderVIPID(uris.getFirst()));
        return result;
    }

    private String getGirderVIPID(Map<String, String> decomposedUri) throws BusinessException {
        List<ExternalPlatform> platforms = externalPlatformBusiness.listAll();

        for (ExternalPlatform platform : platforms) {
            if (platform.getUrl().equals(getGirderURL(decomposedUri))) {
                return platform.getIdentifier();
            }
        }

        return null;
    }

    private String getGirderURL(Map<String, String> decomposedUri) throws BusinessException {
        try {
            URI apiUri = new URI(decomposedUri.get("apiurl"));

            return apiUri.getScheme() + "://" + apiUri.getHost() + 
                (apiUri.getPort() > 0 ? ":" + apiUri.getPort(): "");

        } catch (URISyntaxException e) {
            throw new BusinessException("Query parameter apiurl isn't accepted by URL!", e);
        }
    }

    private Map<String, String> transformURItoMap(String stringUri) throws BusinessException {
        try {
            URI uri = new URI(stringUri);
            Map<String, String> result = new HashMap<>();

            for (String part : uri.getQuery().split("&")) {
                String[] param = part.split("=");

                result.put(param[0], param.length > 1 ? param[1] : null);
            }
            return result;

        } catch (URISyntaxException e) {
            throw new BusinessException("Failed to parse the girder URI", e);
        }
    }

    private Map<String, List<String>> getGirderInputs(String girderID, Map<String, List<String>> inputs, Set<String> editKeys) throws BusinessException {
        Map<String, String> decomposedUri;
        List<String> uris;

        for (String key : editKeys) {
            uris = inputs.get(key);

            for (int i = 0; i < uris.size(); i++) {
                decomposedUri = transformURItoMap(uris.get(i));
                uris.set(i, girderID + ":/" + decomposedUri.get("fileId"));
            }
            inputs.replace(key, uris);
        }
        return inputs;
    }
}
