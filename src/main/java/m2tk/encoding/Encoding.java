/*
 * Copyright (c) Ye Weibin. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package m2tk.encoding;

import m2tk.util.BigEndian;
import m2tk.util.Bytes;
import m2tk.util.CRC32;
import m2tk.util.Preconditions;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

/**
 * Encoding是一个包装类，用来直观描述具有一定编码结构的字节数据。<p>
 * 它具有以下特点：
 * <ol>
 * <li>透明化。Encoding不了解数据结构的任何细节。</li>
 * <li>轻量化。Encoding不存储数据，而是像“外壳”一样将数据包裹起来，并提供一些方便的访问接口。</li>
 * <li>不可变。Encoding是不可变对象，但提供数据固化接口（通过创建一个新的Encoding对象保存数据的副本）。</li>
 * </ol>
 * 鉴于MPEG-2应用环境，Encoding采用BigEndian字节序。
 */
public final class Encoding
{
    private byte[] buf;
    private int off;
    private int len;

    private Encoding(byte[] buffer, int offset, int length)
    {
        buf = buffer;
        off = offset;
        len = length;
    }

    public static Encoding wrap(byte[] buffer)
    {
        return new Encoding(buffer, 0, buffer.length);
    }

    public static Encoding wrap(byte[] buffer, int offset, int length)
    {
        Preconditions.checkFromIndexSize(offset, length, buffer.length);
        return new Encoding(buffer, offset, length);
    }

    public void dispose()
    {
        buf = null;
        off = 0;
        len = 0;
    }

    public Encoding reference()
    {
        return new Encoding(buf, off, len);
    }

    public Encoding copy()
    {
        return wrap(Arrays.copyOfRange(buf, off, off + len));
    }

    public int size()
    {
        return len;
    }

    public byte[] getBytes()
    {
        return Arrays.copyOfRange(buf, off, off + len);
    }

    public byte[] getRange(int from, int to)
    {
        Preconditions.checkFromToIndex(from, to, len);
        return Arrays.copyOfRange(buf, off + from, off + to);
    }

    public int readUINT8(int position)
    {
        Preconditions.checkFromIndexSize(position, 1, len);
        return BigEndian.getUINT8(buf, off + position);
    }

    public int readUINT16(int position)
    {
        Preconditions.checkFromIndexSize(position, 2, len);
        return BigEndian.getUINT16(buf, off + position);
    }

    public int readUINT24(int position)
    {
        Preconditions.checkFromIndexSize(position, 3, len);
        return BigEndian.getUINT24(buf, off + position);
    }

    public long readUINT32(int position)
    {
        Preconditions.checkFromIndexSize(position, 4, len);
        return BigEndian.getUINT32(buf, off + position);
    }

    public long readUINT40(int position)
    {
        Preconditions.checkFromIndexSize(position, 5, len);
        return BigEndian.getUINT40(buf, off + position);
    }

    public long readUINT48(int position)
    {
        Preconditions.checkFromIndexSize(position, 6, len);
        return BigEndian.getUINT48(buf, off + position);
    }

    public long readUINT56(int position)
    {
        Preconditions.checkFromIndexSize(position, 7, len);
        return BigEndian.getUINT56(buf, off + position);
    }

    /**
     * 读取指定位置开始的64位以内（含）的位值。
     * 注意：
     * 纯零首字节将被忽略，例如：mask = 0x00FF，等同于 0xFF；
     * 纯零末字节将被忽略，例如：mask = 0xFF00，等同于 0xFF。
     *
     * @param position 指定位置
     * @param mask     位掩码
     * @return 符合掩码模式的位值（起始于最低有效位）
     */
    public long readBits(int position, long mask)
    {
        Preconditions.checkIndex(position, len);
        checkMask(mask);
        return BigEndian.getBits(buf, off + position, mask);
    }

    /**
     * 读取指定位置开始的4位半字节流（Nibble，非负整数）。
     *
     * @param position 指定位置
     * @param length   半字节流长度（注意：是Nibble的个数，不是字节长度）
     * @return Nibble数组
     */
    public int[] readNibbles(int position, int length)
    {
        Preconditions.checkFromIndexSize(position, length / 2 + length % 2, len);
        int[] nibbles = new int[length];
        int n = (int) (length & 0xFFFFFFFEL);
        for (int i = 0; i < n; i+=2)
        {
            int b = buf[off + position + i / 2] & 0xFF;
            nibbles[i] = b >>> 4;
            nibbles[i + 1] = b & 0xF;
        }
        if (length != n)
        {
            int b = buf[off + position + n / 2] & 0xFF;
            nibbles[length - 1] = b >>> 4;
        }
        return nibbles;
    }

    /**
     * 读取指定位置开始的8位字节流（Octet，非负整数）。
     *
     * @param position 指定位置
     * @param length   字节流长度
     * @return Octet数组
     */
    public int[] readOctets(int position, int length)
    {
        Preconditions.checkFromIndexSize(position, length, len);
        int[] octets = new int[length];
        for (int i = 0; i < length; i++)
            octets[i] = buf[off + position + i] & 0xFF;
        return octets;
    }

    public Encoding readSelector(int position)
    {
        Preconditions.checkFromIndexSize(position, len - position, len);
        return new Encoding(buf, off + position, len - position);
    }

    public Encoding readSelector(int position, int length)
    {
        Preconditions.checkFromIndexSize(position, length, len);
        return new Encoding(buf, off + position, length);
    }

    public int copyRange(int from, int to, ByteArrayOutputStream stream)
    {
        Preconditions.checkFromToIndex(from, to, len);

        int length = to - from;
        stream.write(buf, off + from, to - from);
        return length;
    }

    public int copyRange(int from, int to, OutputStream stream)
            throws IOException
    {
        Preconditions.checkFromToIndex(from, to, len);

        int length = to - from;
        stream.write(buf, off + from, to - from);
        return length;
    }

    public int copyRange(int from, int to, byte[] buffer)
    {
        Preconditions.checkFromToIndex(from, to, len);

        int length = to - from;
        System.arraycopy(buf, off + from, buffer, 0, length);
        return length;
    }

    public int copyRange(int from, int to, byte[] buffer, int offset)
    {
        Preconditions.checkFromToIndex(from, to, len);
        Preconditions.checkFromIndexSize(offset, to - from, buffer.length);

        int length = to - from;
        System.arraycopy(buf, off + from, buffer, offset, length);
        return length;
    }

    public long checksum()
    {
        return CRC32.checksum(buf, off, len);
    }

    public long checksum(int from, int to)
    {
        Preconditions.checkFromToIndex(from, to, len);
        return CRC32.checksum(buf, off + from, to - from);
    }

    public String toHexString()
    {
        return Bytes.toHexString(buf, off, len);
    }

    public String toHexString(int from, int to)
    {
        Preconditions.checkFromToIndex(from, to, len);
        return Bytes.toHexString(buf, off + from, to - from);
    }

    public String toHexStringPrettyPrint()
    {
        return Bytes.toHexStringPrettyPrint(buf, off, len);
    }

    public String toHexStringPrettyPrint(int from, int to)
    {
        Preconditions.checkFromToIndex(from, to, len);
        return Bytes.toHexStringPrettyPrint(buf, off + from, to - from);
    }

    @Override
    public String toString()
    {
        return toHexString();
    }

    public boolean identicalTo(byte[] bytes)
    {
        if (bytes == null)
            return false;

        return identicalTo(bytes, 0, bytes.length);
    }

    public boolean identicalTo(byte[] bytes, int offset, int length)
    {
        if (this.len != length)
            return false;

        return Bytes.equals(this.buf, this.off, bytes, offset, length);
    }

    public boolean identicalTo(Encoding encoding)
    {
        if (encoding == null)
            return false;

        if (this == encoding)
            return true;

        if (this.len != encoding.len)
            return false;

        return Bytes.equals(this.buf, this.off, encoding.buf, encoding.off, this.len);
    }

    public boolean identicalTo(byte[] bytes, byte[] masks)
    {
        return identicalTo(bytes, 0, masks, 0, bytes.length);
    }

    public boolean identicalTo(byte[] bytes, int offset1, byte[] masks, int offset2, int length)
    {
        if (this.len != length)
            return false;

        return Bytes.equalsWithMask(this.buf, this.off,
                                    bytes, offset1,
                                    masks, offset2,
                                    length);
    }

    public boolean identicalTo(Encoding encoding, Encoding masks)
    {
        if (encoding == null || masks == null)
            return false;

        if (this == encoding)
            return true;

        if (this.len != encoding.len)
            return false;

        return Bytes.equalsWithMask(this.buf, this.off,
                                    encoding.buf, encoding.off,
                                    masks.buf, masks.len,
                                    this.len);
    }

    private void checkMask(long mask)
    {
        if (mask == 0)
            throw new IllegalArgumentException("mask must not be all zero");
    }
}
