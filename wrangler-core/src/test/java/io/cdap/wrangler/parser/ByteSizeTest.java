package io.cdap.wrangler.api.parser;

import org.junit.Assert;
import org.junit.Test;

public class ByteSizeTest {

    @Test
    public void testValidSizes() {
        Assert.assertEquals(1024, ByteSize.parse("1KB").getBytes());
        Assert.assertEquals(1_048_576, ByteSize.parse("1MB").getBytes());
        Assert.assertEquals(1_073_741_824, ByteSize.parse("1GB").getBytes());
        Assert.assertEquals(512, ByteSize.parse("512B").getBytes());
        Assert.assertEquals(1.5, ByteSize.parse("1.5MB").getOriginalValue(), 0.0001);
    }

    @Test
    public void testGetMB() {
        ByteSize bs = ByteSize.parse("1048576B");
        Assert.assertEquals(1.0, bs.getMB(), 0.01);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidInput() {
        ByteSize.parse("invalid");
    }
}
