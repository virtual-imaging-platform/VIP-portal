package fr.insalyon.creatis.vip.application.server.business.util;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.web.bindery.requestfactory.server.Pair;

import fr.insalyon.creatis.vip.core.client.VipException;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.datamanager.models.ExternalPlatform;
import fr.insalyon.creatis.vip.datamanager.server.business.ExternalPlatformBusiness;


public class ReproVipInputsParser {

    private ExternalPlatformBusiness externalPlatformBusiness;
    private Map<String, String> providerInformations;
    private Map<String, List<String>> simplifiedInputs;

    enum ProviderType {
        GIRDER,
        LOCAL
    }

    public ReproVipInputsParser(ExternalPlatformBusiness externalPlatformBusiness, String vipURL) throws VipException {
        this.externalPlatformBusiness = externalPlatformBusiness;
        this.providerInformations = new LinkedHashMap<>();

        this.providerInformations.put("vip_url", vipURL);
    }

    public void parse(Map<String, String> inputs) throws VipException {
        simplifiedInputs = getExpandedInputs(inputs);
        Pair<ProviderType, Map<String, List<String>>> type = detectType(simplifiedInputs);
        
        providerInformations.put("storage_type", type.getA().toString());
        switch (type.getA()) {
            case GIRDER:
                providerInformations.putAll(getGirderInformations(type.getB()));
                handleGirderInputs(providerInformations.get("storage_id"), simplifiedInputs, type.getB().keySet());
                break;
            default:
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
        inputs.remove(CoreConstants.RESULTS_DIRECTORY_PARAM_NAME);

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

    private Map<String, String> getGirderInformations(Map<String, List<String>> filteredInputs) throws VipException {
        Map<String, String> result = new HashMap<>();
        Map<String, String> transformUri = transformURItoMap(filteredInputs.values().iterator().next().getFirst());
    
        
        result.put("storage_url", getGirderURL(transformUri));
        result.put("storage_id", getGirderVIPID(transformUri));
        return result;
    }

    private String getGirderVIPID(Map<String, String> decomposedUri) throws VipException {
        List<ExternalPlatform> platforms = externalPlatformBusiness.listAll();

        for (ExternalPlatform platform : platforms) {
            if (platform.getUrl().equals(getGirderURL(decomposedUri))) {
                return platform.getIdentifier();
            }
        }

        return null;
    }

    private String getGirderURL(Map<String, String> decomposedUri) throws VipException {
        try {
            URI apiUri = new URI(decomposedUri.get("apiurl"));

            return apiUri.getScheme() + "://" + apiUri.getHost() + 
                (apiUri.getPort() > 0 ? ":" + apiUri.getPort(): "");

        } catch (URISyntaxException e) {
            throw new VipException("Query parameter apiurl isn't accepted by URL!", e);
        }
    }

    private Map<String, String> transformURItoMap(String stringUri) throws VipException {
        try {
            URI uri = new URI(stringUri);
            Map<String, String> result = new HashMap<>();

            for (String part : uri.getQuery().split("&")) {
                String[] param = part.split("=");

                result.put(param[0], param.length > 1 ? param[1] : null);
            }
            return result;

        } catch (URISyntaxException e) {
            throw new VipException("Failed to parse the girder URI", e);
        }
    }

    private void handleGirderInputs(String girderID, Map<String, List<String>> inputs, Set<String> editKeys) throws VipException {
        Map<String, String> decomposedUri;
        List<String> uris;

        for (String key : editKeys) {
            uris = inputs.get(key);

            for (int i = 0; i < uris.size(); i++) {
                decomposedUri = transformURItoMap(uris.get(i));
                uris.set(i, girderID + ":/" + decomposedUri.get("fileId"));
            }
        }
    }
}
