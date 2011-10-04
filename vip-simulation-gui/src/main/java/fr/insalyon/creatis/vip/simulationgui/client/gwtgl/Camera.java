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
package fr.insalyon.creatis.vip.simulationgui.client.gwtgl;

/**
 *
 * @author Kevin Moulin
 */
public class Camera {

    private int angleX = 0;
    private int angleY = 0;
    private float translateZ = -3;
    private float normalView = -3;
    private float translateX = 0;
    private float translateY = 0;
    private float stepOfViewScroll = 1f;
    private float stepOfViewTranslation = 1f;
    private float ladderOfView = 1f;
    private static Camera instance;

    public static Camera getInstance() {
        if (instance == null) {
            instance = new Camera();
        }
        return instance;
    }

    private Camera() {
    }

    public int getAngleX() {
        return angleX;
    }

    public int getAngleY() {
        return angleY;
    }

    public float getTranslateX() {
        return translateX;
    }

    public float getTranslateY() {
        return translateY;
    }

    public float getTranslateZ() {
        return translateZ;
    }

    public void setAngleX(int x) {
        angleX = x;
    }

    public void setAngleY(int y) {
        angleY = y;
    }

    public void setTranslateX(float x) {
        translateX = translateX + x * ladderOfView * stepOfViewTranslation / 250;
    }

    public void setTranslateY(float y) {
        translateY = translateY + y * ladderOfView * stepOfViewTranslation / 250;
    }

    public void setTranslateZ(float z) {
        translateZ = translateZ - z * ladderOfView * stepOfViewScroll / 25;
    }

    public void setNormalZ(float z) {
        ladderOfView = Math.abs(z / 5);
        normalView = z * 2;
        translateZ = z * 2;
    }

    public void setViewToNormalZ() {
        translateZ = normalView;
    }

    public void setStepOfViewTranslation(int t) {
        stepOfViewTranslation = t;
    }

    public void setStepOfViewScroll(int s) {
        stepOfViewScroll = s;
    }
}
