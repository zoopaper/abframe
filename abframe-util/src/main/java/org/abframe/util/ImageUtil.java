package org.abframe.util;

/**
 * User: shijingui
 * Date: 2015/12/21
 */
public final class ImageUtil {

    /**
     * @param imageName
     * @param isType
     * @return
     */
    public static String getImageSuffix(String imageName, boolean isType) {
        String suffixName = "";
        if (imageName != null && !imageName.equals("")) {
            int index = imageName.lastIndexOf(".");
            if (isType)
                suffixName = imageName.substring(index + 1);
            else
                suffixName = imageName.substring(index);
        }
        return suffixName;
    }
}
