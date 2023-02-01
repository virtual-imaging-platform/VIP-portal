/*
 * Copyright and authors: see LICENSE.txt in base repository.
 *
 * This software is a web portal for pipeline execution on distributed systems.
 *
 * This software is governed by the CeCILL-B license under French law and
 * abiding by the rules of distribution of free software.  You can  use,
 * modify and/ or redistribute the software under the terms of the CeCILL-B
 * license as circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info".
 *
 * As a counterpart to the access to the source code and  rights to copy,
 * modify and redistribute granted by the license, users are provided only
 * with a limited warranty  and the software's author,  the holder of the
 * economic rights,  and the successive licensors  have only  limited
 * liability.
 *
 * In this respect, the user's attention is drawn to the risks associated
 * with loading,  using,  modifying and/or developing or reproducing the
 * software by the user in light of its specific status of free software,
 * that may mean  that it is complicated to manipulate,  and  that  also
 * therefore means  that it is reserved for developers  and  experienced
 * professionals having in-depth computer knowledge. Users are therefore
 * encouraged to load and test the software's suitability as regards their
 * requirements in conditions enabling the security of their systems and/or
 * data to be ensured and,  more generally, to use and operate it in the
 * same conditions as regards security.
 *
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL-B license and that you accept its terms.
 */

package fr.insalyon.creatis.vip.applicationimporter.server.business;

import fr.insalyon.creatis.vip.application.client.bean.boutiquesTools.BoutiquesApplication;

import java.io.StringWriter;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.tools.generic.EscapeTool;
import org.springframework.stereotype.Service;

/**
 *
 * @author Tristan Glatard
 */
@Service
public class VelocityUtils {

    private VelocityEngine velocityEngine;

    private VelocityUtils() {
        velocityEngine = new VelocityEngine();
        velocityEngine.setProperty("resource.loader", "class");
        velocityEngine.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        velocityEngine.init();
    }

    public String createDocument(BoutiquesApplication bt, String vmTemplate) {
        VelocityContext context = new VelocityContext();
        context.put("tool", bt);
        context.put("esc", new EscapeTool());

        StringWriter stringWriter = new StringWriter();

        Template template = velocityEngine.getTemplate(vmTemplate);
        template.merge(context, stringWriter);

        return stringWriter.toString();
    }

    public String createDocument(BoutiquesApplication bt, String fileAccessProtocol, String vmTemplate) {
        VelocityContext context = new VelocityContext();
        context.put("tool", bt);
        context.put("esc", new EscapeTool());
        context.put("fileAccessProtocol", fileAccessProtocol);

        StringWriter stringWriter = new StringWriter();

        Template template = velocityEngine.getTemplate(vmTemplate);
        template.merge(context, stringWriter);

        return stringWriter.toString();
    }

    public String createDocument( String tag, BoutiquesApplication bt, Boolean isRunOnGrid, String vmTemplate) {
        VelocityContext context = new VelocityContext();
        context.put("tag", tag);
        context.put("tool", bt);
        context.put("isRunOnGrid", isRunOnGrid);
        context.put("esc", new EscapeTool());

        StringWriter stringWriter = new StringWriter();

        Template template = velocityEngine.getTemplate(vmTemplate);
        template.merge(context, stringWriter);

        return stringWriter.toString();
    }

    public String createDocument(String tag, BoutiquesApplication bt, Boolean isRunOnGrid, String fileAccessProtocol, String vmTemplate) {
        VelocityContext context = new VelocityContext();
        context.put("tag", tag);
        context.put("tool", bt);
        context.put("isRunOnGrid", isRunOnGrid);
        context.put("esc", new EscapeTool());
        context.put("fileAccessProtocol", fileAccessProtocol);

        StringWriter stringWriter = new StringWriter();

        Template template = velocityEngine.getTemplate(vmTemplate);
        template.merge(context, stringWriter);

        return stringWriter.toString();
    }
}
