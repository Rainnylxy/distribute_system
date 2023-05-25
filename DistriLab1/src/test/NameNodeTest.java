package test;
import impl.ClientImpl;
import utils.FileDesc;
import api.NameNode;
import impl.NameNodeImpl;
import org.junit.Before;
import org.junit.Test;
import utils.FileSystem;

import static org.junit.Assert.*;

//mode: 0:read  1£ºwrite  2£ºread and write
public class NameNodeTest {
    private static NameNodeImpl nn;
    private static ClientImpl client = new ClientImpl();
    private void close(FileDesc... fileInfos){
        for(FileDesc fileInfo: fileInfos){
            nn.close(fileInfo.toString());
        }
    }

    @Before
    public void setUp(){
        nn = new NameNodeImpl();
    }

    @Test
    /* open a non-exist file */
    public void testCreate(){
        String filename = FileSystem.newFilename();
        int fd = client.open(filename, 0b01);
        FileDesc fileInfo = client.getFdesc(fd);
        assertNotNull(fileInfo);
        close(fileInfo);
    }

    @Test
    /* open an existing file */
    public void testOpen(){
        String filename = FileSystem.newFilename();
        int fd = client.open(filename, 0b01);
        FileDesc fileInfo = client.getFdesc(fd);
        int fd2 = client.open(filename, 0b00);
        FileDesc fileInfo2 = client.getFdesc(fd2);
        assertNotSame(fileInfo,fileInfo2);
        close(fileInfo, fileInfo2);
    }



    @Test
    /* open an existing and being written file in writing mode */
    public void testOpenWrite(){
        String filename = FileSystem.newFilename();
        int fd = client.open(filename, 0b01);
        FileDesc fileInfo = client.getFdesc(fd);
        int fd2 = client.open(filename, 0b10);
        FileDesc fileInfo2 = client.getFdesc(fd2);
        assertNotNull(fileInfo);
        assertNull(fileInfo2);
        close(fileInfo);
    }

    @Test
    /* open an existing and being written file in reading mode, multiple times */
    public void testOpenRead(){
        String filename = FileSystem.newFilename();
        int fd = client.open(filename, 0b01);
        FileDesc fileInfo = client.getFdesc(fd);
        int fd2 = client.open(filename, 0b00);
        FileDesc fileInfo2 = client.getFdesc(fd2);
        int fd3 = client.open(filename, 0b00);
        FileDesc fileInfo3 = client.getFdesc(fd3);
        assertNotNull(fileInfo);
        assertNotNull(fileInfo2);
        assertNotNull(fileInfo3);
        close(fileInfo,fileInfo2,fileInfo3);
    }
}
