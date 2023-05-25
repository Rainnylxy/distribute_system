package test;

import api.Client;
import impl.ClientImpl;
import org.junit.Before;
import org.junit.Test;
import utils.FileSystem;

import static org.junit.Assert.*;
import java.nio.charset.StandardCharsets;
//mode: 0:read  1£ºwrite  2£ºread and write
public class ClientTest {
    static Client client;
    @Before
    public void setUp(){
        client = new ClientImpl();
    }

    @Test
    public void testWriteRead() throws InterruptedException {
        String filename = FileSystem.newFilename();
        int fd = client.open(filename, 0b10);
        client.append(fd,"hello".getBytes(StandardCharsets.UTF_8));
        assertArrayEquals(client.read(fd),"hello".getBytes(StandardCharsets.UTF_8));
        client.append(fd," world".getBytes(StandardCharsets.UTF_8));
        assertArrayEquals(client.read(fd),"hello world".getBytes(StandardCharsets.UTF_8));
        client.close(fd);
    }

    @Test
    public void testWriteFail() throws InterruptedException {
        String filename = FileSystem.newFilename();
        int fd = client.open(filename,0b00);
        client.append(fd,"Lala-land".getBytes(StandardCharsets.UTF_8));
        assertArrayEquals(client.read(fd),"".getBytes(StandardCharsets.UTF_8));
        client.close(fd);
    }

    @Test
    public void testReadFail(){
        String filename = FileSystem.newFilename();
        int fd = client.open(filename,0b01);
        assertNull(client.read(fd));
        client.close(fd);
    }
}
