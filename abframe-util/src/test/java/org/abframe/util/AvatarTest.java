package org.abframe.util;

import net.coobird.thumbnailator.Thumbnails;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

/**
 * User: shijingui
 * Date: 2015/12/22
 */
public class AvatarTest {

    @Test
    public void cut() {
        try {
            Thumbnails.of(new File("d:/tulips.jpg")).sourceRegion(541,207, 262, 197).size(262,197).toFile(new File("d:/a.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
