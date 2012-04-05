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

import com.google.gwt.user.client.rpc.IsSerializable;
import java.util.LinkedHashMap;
import java.util.TreeMap;

/**
 *
 * @author Rafael Silva
 */
public enum CountryCode implements IsSerializable {

    ad("Andorra"),
    ae("United Arab Emirates"),
    af("Afghanistan"),
    ag("Antigua and Barbuda"),
    ai("Anguilla"),
    al("Albania"),
    am("Armenia"),
    an("Netherlands Antilles"),
    ao("Angola"),
    aq("Antarctica"),
    ar("Argentina"),
    as("American Samoa"),
    at("Austria"),
    au("Australia"),
    aw("Aruba"),
    ax("Aland Islands"),
    az("Azerbaijan"),
    ba("Bosnia and Herzegovina"),
    bb("Barbados"),
    bd("Bangladesh"),
    be("Belgium"),
    bf("Burkina Faso"),
    bg("Bulgaria"),
    bh("Bahrain"),
    bi("Burundi"),
    bj("Benin"),
    bm("Bermuda"),
    bn("Brunei Darussalam"),
    bo("Bolivia"),
    br("Brazil"),
    bs("Bahamas"),
    bt("Bhutan"),
    bv("Bouvet Island"),
    bw("Botswana"),
    by("Belarus"),
    bz("Belize"),
    ca("Canada"),
    cc("Cocos (Keeling) Islands"),
    cd("Democratic Republic of the Congo"),
    cf("Central African Republic"),
    cg("Congo"),
    ch("Switzerland"),
    ci("Cote D'Ivoire (Ivory Coast)"),
    ck("Cook Islands"),
    cl("Chile"),
    cm("Cameroon"),
    cn("China"),
    co("Colombia"),
    cr("Costa Rica"),
    cs("Serbia and Montenegro"),
    cu("Cuba"),
    cv("Cape Verde"),
    cx("Christmas Island"),
    cy("Cyprus"),
    cz("Czech Republic"),
    de("Germany"),
    dj("Djibouti"),
    dk("Denmark"),
    dm("Dominica"),
    dz("Algeria"),
    ec("Ecuador"),
    ee("Estonia"),
    eg("Egypt"),
    eh("Western Sahara"),
    er("Eritrea"),
    es("Spain"),
    et("Ethiopia"),
    fi("Finland"),
    fj("Fiji"),
    fk("Falkland Islands (Malvinas)"),
    fm("Federated States of Micronesia"),
    fo("Faroe Islands"),
    fr("France"),
    ga("Gabon"),
    gb("Great Britain (UK)"),
    gd("Grenada"),
    ge("Georgia"),
    gf("French Guiana"),
    gh("Ghana"),
    gi("Gibraltar"),
    gl("Greenland"),
    gm("Gambia"),
    gn("Guinea"),
    gp("Guadeloupe"),
    gq("Equatorial Guinea"),
    gr("Greece"),
    gs("S. Georgia and S. Sandwich Islands"),
    gt("Guatemala"),
    gu("Guam"),
    gw("Guinea-Bissau"),
    gy("Guyana"),
    hk("Hong Kong"),
    hm("Heard Island and McDonald Islands"),
    hn("Honduras"),
    hr("Croatia (Hrvatska)"),
    ht("Haiti"),
    hu("Hungary"),
    id("Indonesia"),
    ie("Ireland"),
    il("Israel"),
    in("India"),
    io("British Indian Ocean Territory"),
    iq("Iraq"),
    ir("Iran"),
    is("Iceland"),
    it("Italy"),
    jm("Jamaica"),
    jo("Jordan"),
    jp("Japan"),
    ke("Kenya"),
    kg("Kyrgyzstan"),
    kh("Cambodia"),
    ki("Kiribati"),
    km("Comoros"),
    kn("Saint Kitts and Nevis"),
    kp("Korea (North)"),
    kr("Korea (South)"),
    kw("Kuwait"),
    ky("Cayman Islands"),
    kz("Kazakhstan"),
    la("Laos"),
    lb("Lebanon"),
    lc("Saint Lucia"),
    li("Liechtenstein"),
    lk("Sri Lanka"),
    lr("Liberia"),
    ls("Lesotho"),
    lt("Lithuania"),
    lu("Luxembourg"),
    lv("Latvia"),
    ly("Libya"),
    ma("Morocco"),
    mc("Monaco"),
    md("Moldova"),
    mg("Madagascar"),
    mh("Marshall Islands"),
    mk("Macedonia"),
    ml("Mali"),
    mm("Myanmar"),
    mn("Mongolia"),
    mo("Macao"),
    mp("Northern Mariana Islands"),
    mq("Martinique"),
    mr("Mauritania"),
    ms("Montserrat"),
    mt("Malta"),
    mu("Mauritius"),
    mv("Maldives"),
    mw("Malawi"),
    mx("Mexico"),
    my("Malaysia"),
    mz("Mozambique"),
    na("Namibia"),
    nc("New Caledonia"),
    ne("Niger"),
    nf("Norfolk Island"),
    ng("Nigeria"),
    ni("Nicaragua"),
    nl("Netherlands"),
    no("Norway"),
    np("Nepal"),
    nr("Nauru"),
    nu("Niue"),
    nz("New Zealand (Aotearoa)"),
    om("Oman"),
    pa("Panama"),
    pe("Peru"),
    pf("French Polynesia"),
    pg("Papua New Guinea"),
    ph("Philippines"),
    pk("Pakistan"),
    pl("Poland"),
    pm("Saint Pierre and Miquelon"),
    pn("Pitcairn"),
    pr("Puerto Rico"),
    ps("Palestinian Territory"),
    pt("Portugal"),
    pw("Palau"),
    py("Paraguay"),
    qa("Qatar"),
    re("Reunion"),
    ro("Romania"),
    ru("Russian Federation"),
    rw("Rwanda"),
    sa("Saudi Arabia"),
    sb("Solomon Islands"),
    sc("Seychelles"),
    sd("Sudan"),
    se("Sweden"),
    sg("Singapore"),
    sh("Saint Helena"),
    si("Slovenia"),
    sj("Svalbard and Jan Mayen"),
    sk("Slovakia"),
    sl("Sierra Leone"),
    sm("San Marino"),
    sn("Senegal"),
    so("Somalia"),
    sr("Suriname"),
    st("Sao Tome and Principe"),
    sv("El Salvador"),
    sy("Syria"),
    sz("Swaziland"),
    tc("Turks and Caicos Islands"),
    td("Chad"),
    tg("Togo"),
    th("Thailand"),
    tj("Tajikistan"),
    tk("Tokelau"),
    tl("Timor-Leste"),
    tm("Turkmenistan"),
    tn("Tunisia"),
    to("Tonga"),
    tp("East Timor"),
    tr("Turkey"),
    tt("Trinidad and Tobago"),
    tv("Tuvalu"),
    tw("Taiwan"),
    tz("Tanzania"),
    ua("Ukraine"),
    ug("Uganda"),
    uk("United Kingdom"),
    us("United States"),
    uy("Uruguay"),
    uz("Uzbekistan"),
    va("Vatican City State (Holy See)"),
    vc("Saint Vincent and the Grenadines"),
    ve("Venezuela"),
    vg("Virgin Islands (British)"),
    vi("Virgin Islands (U.S.)"),
    vn("Viet Nam"),
    vu("Vanuatu"),
    wf("Wallis and Futuna"),
    ws("Samoa"),
    ye("Yemen"),
    yt("Mayotte"),
    za("South Africa"),
    zm("Zambia"),
    zw("Zimbabwe");
    private String name;

    private CountryCode(String name) {
        this.name = name;
    }

    public String getCountryName() {
        return name;
    }

    public static LinkedHashMap<String, String> getCountriesMap() {

        TreeMap<String, String> treeMap = new TreeMap<String, String>();
        for (CountryCode cc : CountryCode.values()) {
            treeMap.put(cc.getCountryName(), cc.name());
        }


        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();

        for (String s : treeMap.keySet()) {
            map.put(treeMap.get(s), s);
        }

        return map;
    }

    public static LinkedHashMap<String, String> getCodesMap() {

        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();

        for (CountryCode cc : CountryCode.values()) {
            map.put(cc.name(), cc.name());
        }

        return map;
    }
}
