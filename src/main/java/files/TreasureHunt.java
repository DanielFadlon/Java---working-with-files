package files;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class TreasureHunt {
   /**
     * Find the treasure by following the map.
     *
     * Starting with the firstClue, and until it has found the treasure (the decoder returned -1), this method should
     * repeat the following actions:
     * <ol>
     *     <li>Decode the clue (using the decoder) to get the index of the next clue. </li>
     *     <li>Read the next clue from the map. Each clue consists of the 48 bits of the map starting at the index
     *     returned from the decoder (treat anything beyond the end of the map as 0).
     *
     *     The index of a clue is given in bits from the beginning of the map,
     *     where 0 is the MSB of the first byte (and map_size_in_bytes*8-1 is the LSB of the last byte). </li>
     * </ol>
     *
     *
     * @param map This is a {@link FileChannel} containing the encoded treasure map.
     * @param decoder The decoder used to find the location of the next clue
     * @param firstClue The first clue.
     * @return The index of the treasure in the file (in bits)
     */
    public static long findTreasure(FileChannel map, TreasureMapDecoder decoder, long firstClue) throws IOException {
        long currentClue = firstClue;
        long indexClue = -1;
        long indexNext;
        int gap = 0;
        int inMap = 0;
        ByteBuffer buf = ByteBuffer.allocate(7);
        try {

            while ((indexNext = decoder.decodeClue(currentClue, indexClue, map.size() * 8)) != -1) {
                indexClue = indexNext;
                /* gap is the number of first bits before the indexClue */
                gap = (int) (indexClue % 8);
                map.position(indexClue / 8);
                /* reset buf position to 0 */
                buf.position(0);
                inMap = map.read(buf);
                currentClue = valueOf(buf, gap, inMap);
            }
        }catch(IOException e){
            System.err.println("Error:" + e);
        }
        return indexClue;
    }

    /**
     *Compute value of 48 bits in ByteBuffer from start index(in bits).
     * Note that the function read 48 bits also if the buffer is smaller than that.
     * Any bit beyond the buffer treat like 0.
     *
     * @param buf- ByteBuffer of 7 bytes.
     * @param index- start index (in bits).
     * @param inMap - the number of bytes that read to buf.
     * @return long ans - the value of 48 bits from index mod (in bits).
     */
    private static long valueOf(ByteBuffer buf , long index, long inMap) {
        long ans = 0;
        /* update map to number of bits*/
        inMap *=8;

        /* for bit i in range [index - 48 + index -1 ]
         * if the bit is inMap then add him to ans
         * otherwise , add 0 */
        for(int i = (int) index; i < (48 + index); i++){
            ans <<= 1;
            if(i < inMap) {
                ans +=  ((buf.get(i / 8) >> (8 - (i % 8) - 1)) & 0X0001);
            }
        }
        return ans;
    }
}
