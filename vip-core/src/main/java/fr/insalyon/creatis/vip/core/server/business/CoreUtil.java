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
package fr.insalyon.creatis.vip.core.server.business;

import java.text.Normalizer;

public class CoreUtil {

    /*
        remove accents and non-ascii characters
    */
    public static String getCleanString(String s, String replacement) {
        return getCleanString(s, true, true, replacement);
    }

    public static String getCleanStringAlnum(String s, String replacement) {
        String result = getCleanString(s, replacement);

        return result.replaceAll("[^a-zA-Z0-9]", replacement);
    }

    public static String getCleanString(String s, boolean removeAccents, boolean onlyKeepAscii, String replacement) {
        if ( removeAccents) {
            // Normalizer.normalize with NFKD form decompose accentuated
            // letters into separate "accent mark + base letter" characters
            s = Normalizer.normalize(s, Normalizer.Form.NFKD);
        }

        if (onlyKeepAscii) {
            // the [^\\p{ASCII}] regex remove all non-ascii characters
            // so also the separated accents char if removeAccents is true
            return s.replaceAll("[^\\p{ASCII}]", replacement);
        } else if (removeAccents) {
            // the '\p{M}' regex remove all the combining marks (accents and
            // other exotic stuff) and is less strict than onlyKeepAscii
            // for instance it will keep 'œ' '«' '»' '°'
            return s.replaceAll("\\p{M}", replacement);
        } else {
            // nothing to do
            return s;
        }
    }
}
