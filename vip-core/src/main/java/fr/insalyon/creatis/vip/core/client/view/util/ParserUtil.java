/* Copyright CNRS-CREATIS
 *
 * Rafael Silva
 * rafael.silva@creatis.insa-lyon.fr
 * http://www.rafaelsilva.com
 *
 * This software is a grid-enabled data-driven workflow manager and editor.
 *
 * This software is governed by the CeCILL  license under French law and
 * abiding by the rules of distribution of free software.  You can  use,
 * modify and/ or redistribute the software under the terms of the CeCILL
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
 * knowledge of the CeCILL license and that you accept its terms.
 */
package fr.insalyon.creatis.vip.core.client.view.util;

/**
 *
 * @author Rafael Silva
 */
public class ParserUtil {

    public static String parseLocation(String countryCode) {


        if (countryCode.equals("bg")) {
            return "Bulgaria";
        } else if (countryCode.equals("br")) {
            return "Brazil";
        } else if (countryCode.equals("co")) {
            return "Colombia";
        } else if (countryCode.equals("cl")) {
            return "Chile";
        } else if (countryCode.equals("de")) {
            return "Germany";
        } else if (countryCode.equals("es")) {
            return "Spain";
        } else if (countryCode.equals("fr")) {
            return "France";
        } else if (countryCode.equals("gr")) {
            return "Greece";
        } else if (countryCode.equals("ie")) {
            return "Ireland";
        } else if (countryCode.equals("it")) {
            return "Italy";
        } else if (countryCode.equals("nl")) {
            return "Netherlands";
        } else if (countryCode.equals("pl")) {
            return "Poland";
        } else if (countryCode.equals("pt")) {
            return "Portugal";
        } else if (countryCode.equals("ru")) {
            return "Russia";
        } else if (countryCode.equals("uk")) {
            return "United Kingdom";
        }

        return "Unknown";
    }
}
