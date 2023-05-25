package test;

import api.DataNode;
import impl.DataNodeImpl;
import org.junit.Before;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.Assert.*;

//mode: 0:read  1£ºwrite  2£ºread and write
public class DataNodeTest {
    static DataNodeImpl dn;

    @Before
    public void setUp() {
        dn = new DataNodeImpl("1051");
    }

    @Test
    public void testRead() {
        int blockId = 1;
        assertNotNull(dn.read(blockId));
    }

    @Test
    public void testAppend() {
        int blockId = 5;
        byte[] toWrite = "Hello World".getBytes(StandardCharsets.UTF_8);

        dn.append(blockId, toWrite,"hello",1);
        byte[] read = dn.read(blockId);

        ArrayList<Byte> readNotNull=new ArrayList<>();
        for(int i=0;i<read.length;i++){
            if(read[i]!=0x00){
                readNotNull.add(read[i]);
            }
        }

        int n = toWrite.length;
        int N = readNotNull.size();
        assertEquals("equals",n,N);
        for (int i = 0; i < n; i++) {
            assertEquals("Block ID: " + blockId + ". Read block bytes and appended bytes differ at the " + i
                    + " byte to the eof.", toWrite[n - 1 - i], readNotNull.get(N-1-i).byteValue());
        }
    }
}
