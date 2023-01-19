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
package fr.insalyon.creatis.vip.api.data;

import fr.insalyon.creatis.vip.api.model.SignUpUserDTO;
import fr.insalyon.creatis.vip.api.security.apikey.SpringApiPrincipal;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.client.view.user.UserLevel;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

/**
 * Created by abonnet on 7/26/16.
 */
public class UserTestUtils {

    static public User baseUser1;
    static public User baseUser2;

    static public SignUpUserDTO restUser1;

    static public String baseUser1Password = "baseUser1password";
    static public String baseUser2Password = "baseUser2password";


    static {
        baseUser1 = new User("base1", "User1", "baseuser1@test.tst", null,
                UserLevel.Beginner, null);
        baseUser1.setFolder("user1");
        baseUser2 = new User("base2", "User2", "baseuser2@test.tst", null,
                UserLevel.Advanced, null);
        baseUser2.setFolder("user2");

        restUser1 = new SignUpUserDTO("base3", "User3", "baseuser3@test.tst", "test", baseUser2Password,
                UserLevel.Advanced, null, "test comment", "test applications");
    }

    public static RequestPostProcessor baseUser1() {
        return SecurityMockMvcRequestPostProcessors.user(new SpringApiPrincipal(baseUser1));
    }

    public static RequestPostProcessor baseUser2() {
        return SecurityMockMvcRequestPostProcessors.user(new SpringApiPrincipal(baseUser2));
    }
}
