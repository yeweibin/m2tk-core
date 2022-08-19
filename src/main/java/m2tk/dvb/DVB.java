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

package m2tk.dvb;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.*;

public final class DVB
{
    private DVB()
    {
    }

    public static String decodeSatelliteFrequencyCode(long code)
    {
        int d1 = (int) ((code >> 28) & 0xF);
        int d2 = (int) ((code >> 24) & 0xF);
        int d3 = (int) ((code >> 20) & 0xF);
        int d4 = (int) ((code >> 16) & 0xF);
        int d5 = (int) ((code >> 12) & 0xF);
        int d6 = (int) ((code >> 8) & 0xF);
        int d7 = (int) ((code >> 4) & 0xF);
        int d8 = (int) ((code) & 0xF);
        int p1 = d1 * 100 + d2 * 10 + d3;
        int p2 = d4 * 10000 + d5 * 1000 + d6 * 100 + d7 * 10 + d8;
        return String.format("%d.%05d", p1, p2);
    }

    public static String decodeCableFrequencyCode(long code)
    {
        int d1 = (int) ((code >> 28) & 0xF);
        int d2 = (int) ((code >> 24) & 0xF);
        int d3 = (int) ((code >> 20) & 0xF);
        int d4 = (int) ((code >> 16) & 0xF);
        int d5 = (int) ((code >> 12) & 0xF);
        int d6 = (int) ((code >> 8) & 0xF);
        int d7 = (int) ((code >> 4) & 0xF);
        int d8 = (int) ((code) & 0xF);
        int p1 = d1 * 1000 + d2 * 100 + d3 * 10 + d4;
        int p2 = d5 * 1000 + d6 * 100 + d7 * 10 + d8;
        return String.format("%d.%04d", p1, p2);
    }

    public static long encodeCableFrequencyCode(String frequency)
    {
        int offset = frequency.indexOf('.');
        long p1 = Integer.parseInt(frequency.substring(0, offset));
        long p2 = Integer.parseInt(frequency.substring(offset + 1));
        long d1 = p1 / 1000;
        long d2 = p1 % 1000 / 100;
        long d3 = p1 % 100 / 10;
        long d4 = p1 % 10;
        long d5 = p2 / 1000;
        long d6 = p2 % 1000 / 100;
        long d7 = p2 % 100 / 10;
        long d8 = p2 % 10;
        return ((d1 << 28) | (d2 << 24) | (d3 << 20) | (d4 << 16) |
                (d5 << 12) | (d6 << 8) | (d7 << 4) | (d8));
    }

    public static String decodeSymbolRateCode(int code)
    {
        int d1 = (code >> 24) & 0xF;
        int d2 = (code >> 20) & 0xF;
        int d3 = (code >> 16) & 0xF;
        int d4 = (code >> 12) & 0xF;
        int d5 = (code >> 8) & 0xF;
        int d6 = (code >> 4) & 0xF;
        int d7 = (code) & 0xF;
        int p1 = d1 * 100 + d2 * 10 + d3;
        int p2 = d4 * 1000 + d5 * 100 + d6 * 10 + d7;
        return String.format("%d.%04d", p1, p2);
    }

    public static int encodeSymbolRateCode(String rate)
    {
        int offset = rate.indexOf('.');
        int p1 = Integer.parseInt(rate.substring(0, offset));
        int p2 = Integer.parseInt(rate.substring(offset + 1));
        int d1 = p1 / 100;
        int d2 = p1 % 100 / 10;
        int d3 = p1 % 10;
        int d4 = p2 / 1000;
        int d5 = p2 % 1000 / 100;
        int d6 = p2 % 100 / 10;
        int d7 = p2 % 10;
        return (d1 << 24) | (d2 << 20) | (d3 << 16) |
               (d4 << 12) | (d5 << 8) | (d6 << 4) | d7;
    }

    private static int encode_bcd_value(int value)
    {
        return ((value / 10) << 4) | (value % 10);
    }

    private static int decode_bcd_value(int bcd)
    {
        return ((bcd >> 4) & 0xF) * 10 + (bcd & 0xF);
    }

    private static int encode_time_fields(int seconds)
    {
        int hh = seconds / 3600;
        int mm = seconds % 3600 / 60;
        int ss = seconds % 60;
        return (encode_bcd_value(hh) << 16) |
               (encode_bcd_value(mm) << 8) |
               (encode_bcd_value(ss));
    }

    private static int decode_time_fields(int fields)
    {
        int hh = decode_bcd_value((fields >> 16) & 0xFF);
        int mm = decode_bcd_value((fields >> 8) & 0xFF);
        int ss = decode_bcd_value((fields) & 0xFF);
        return hh * 3600 + mm * 60 + ss;
    }

    public static long encodeTimepointFromLocalDateTime(LocalDateTime datetime)
    {
        LocalDateTime utcDateTime = ZonedDateTime.of(datetime, ZoneId.systemDefault())
                                                 .withZoneSameInstant(ZoneId.of("UTC"))
                                                 .toLocalDateTime();
        long mjd = encodeDate(utcDateTime.toLocalDate());
        long utc = encodeTime(utcDateTime.toLocalTime());
        return (mjd << 24) | utc;
    }

    public static LocalDateTime decodeTimepointIntoLocalDateTime(long timepoint)
    {
        // timepoint = [MJD:2]+[UTC:3]
        int mjd = (int) ((timepoint >> 24) & 0x0000FFFFL);
        int utc = (int) ((timepoint) & 0x00FFFFFFL);

        LocalDate date = decodeDate(mjd);
        LocalTime time = decodeTime(utc);
        return ZonedDateTime.of(date, time, ZoneId.of("UTC"))
                            .withZoneSameInstant(ZoneId.systemDefault())
                            .toLocalDateTime();
    }

    public static String printTimeFields(int fields)
    {
        int hh = decode_bcd_value((fields >> 16) & 0xFF);
        int mm = decode_bcd_value((fields >> 8) & 0xFF);
        int ss = decode_bcd_value((fields) & 0xFF);
        return String.format("%02d:%02d:%02d", hh, mm, ss);
    }

    public static int decodeDuration(int duration)
    {
        return decode_time_fields(duration);
    }

    public static int encodeDuration(int seconds)
    {
        return encode_time_fields(seconds);
    }

    public static int encodeTime(LocalTime time)
    {
        int seconds = time.toSecondOfDay();
        return encode_time_fields(seconds);
    }

    public static LocalTime decodeTime(int fields)
    {
        int seconds = decode_time_fields(fields);
        return LocalTime.ofSecondOfDay(seconds);
    }

    public static int encodeDate(LocalDate date)
    {
        return compute24BitMJDFromLocalDate(date);
    }

    public static LocalDate decodeDate(int mjd)
    {
        int mjd24 = adjustMJDFrom16BitTo24Bit(mjd);
        return computeCalendarDateFrom24BitMJD(mjd24);
    }

    private static int adjustMJDFrom16BitTo24Bit(int mjd)
    {
        /* MJD计算公式适用于 1900-03-01 到 2100-02-28 之间的日期，
         * 其值随日期增长而线性增加，范围在[0x3AE7, 0x1583F]。
         *
         * 对于以16位整数存储MJD值，将会出现“上溢归零”现象。即
         * MJD(0xFFFF) + 1 -> MJD(0x0000)，与之对应的日期为 2038-04-22 和 2038-04-23。
         * 也就是说，16位MJD值无法表示 2038-04-22 之后的日期，需要将其扩容（至24位）。
         *
         * 由于MJD是线性增长的，对于公元2000年以后的日期，其MJD值早已超过0x583F，
         * 因此可以将[0x0, 0x583F]内的数值修正为[0x10000, 0x1583F]，从而实现对16位MJD值
         * 的平滑扩容。
         */

        return (mjd > 0x583F) ? mjd : mjd | 0x10000;
    }

    private static LocalDate computeCalendarDateFrom24BitMJD(int mjd)
    {
        /**
         * 从MJD值中还原出年、月、日。
         *
         *    Y' = int [ (MJD - 15078.2) / 365.25 ]
         *    M' = int { [ MJD - 14 956.1 - int (Y' × 365.25) ] / 30.6001 }
         *    D = MJD - 14 956 - int (Y' × 365.25) - int (M' × 30.6001)
         *    If M' = 14 or M' = 15, then K = 1; else K = 0
         *    Y = Y' + K
         *    M = M' - 1 - K × 12
         *
         *  注意：年份从1900开始计算，因此，对于2010年，Y等于110。
         */

        int y, m, d, k;

        y = (int) ((mjd - 15078.2) / 365.25);
        m = (int) ((mjd - 14956.1 - (int) (y * 365.25)) / 30.6001);
        d = mjd - 14956 - (int) (y * 365.25) - (int) (m * 30.6001);
        k = (m == 14 || m == 15) ? 1 : 0;

        int year = 1900 + y + k;
        int month = m - 1 - k * 12;
        int day = d;
        return LocalDate.of(year, month, day);
    }

    private static int compute24BitMJDFromLocalDate(LocalDate date)
    {
        /* 根据年、月、日计算MJD值。
         *
         *    MJD = 14956 + D + int [(Y - K) × 365.25] + int [(M + 1 + K × 12) × 30.6001]
         *    If M = 1 or M = 2, then K = 1; else K = 0
         *
         *  注意：年份从1900开始计算，因此，对于2010年，Y等于110。
         */

        int y = date.getYear() - 1900;
        int m = date.getMonthValue();
        int d = date.getDayOfMonth();
        int k = (m == 1 || m == 2) ? 1 : 0;

        return 14956 + d + (int) ((y - k) * 365.25) + (int) ((m + 1 + k * 12) * 30.6001);
    }

    public static String decodeThreeLetterCode(int code)
    {
        byte[] bytes = new byte[3];
        bytes[0] = (byte) ((code >> 16) & 0xFF);
        bytes[1] = (byte) ((code >> 8) & 0xFF);
        bytes[2] = (byte) ((code) & 0xFF);
        return new String(bytes);
    }

    /*
     * 符合国标（GB/T 28161-2011）要求的字符串编解码器。
     * 注意：国标对 DVB 规范（ETSI EN-300.468）进行了适当的裁剪，省略了部分拉丁字符集。
     *      鉴于国内不规范的编解码方式，“默认字符集”设为 GBK 而非规范中定义的拉丁字符。
     */
    public static String decodeString(byte[] bytes)
    {
        return decodeString(bytes, 0, bytes.length);
    }

    public static String decodeString(byte[] bytes, int offset, int length)
    {
        if (length == 0)
            return "";

        int first_byte = bytes[offset] & 0xFF;

        if (0x01 <= first_byte && first_byte <= 0x0B)
            return construct_string_safely(bytes, offset + 1, length - 1, "UTF-8");

        if (first_byte == 0x10)
        {
            int second_byte = bytes[offset + 1] & 0xFF;
            int third_byte = bytes[offset + 2] & 0xFF;

            if (second_byte != 0x00 || third_byte >= 0x10)
                return construct_string_safely(bytes, offset + 3, length - 3, "UTF-8");

            switch (third_byte)
            {
                case 0x01:
                    return construct_string_safely(bytes, offset + 3, length - 3, "ISO-8859-1");
                case 0x02:
                    return construct_string_safely(bytes, offset + 3, length - 3, "ISO-8859-2");
                case 0x03:
                    return construct_string_safely(bytes, offset + 3, length - 3, "ISO-8859-3");
                case 0x04:
                    return construct_string_safely(bytes, offset + 3, length - 3, "ISO-8859-4");
                case 0x05:
                    return construct_string_safely(bytes, offset + 3, length - 3, "ISO-8859-5");
                case 0x06:
                    return construct_string_safely(bytes, offset + 3, length - 3, "ISO-8859-6");
                case 0x07:
                    return construct_string_safely(bytes, offset + 3, length - 3, "ISO-8859-7");
                case 0x08:
                    return construct_string_safely(bytes, offset + 3, length - 3, "ISO-8859-8");
                case 0x09:
                    return construct_string_safely(bytes, offset + 3, length - 3, "ISO-8859-9");
                case 0x0A:
                    return construct_string_safely(bytes, offset + 3, length - 3, "ISO-8859-10");
                case 0x0B:
                    return construct_string_safely(bytes, offset + 3, length - 3, "ISO-8859-11");
                case 0x0D:
                    return construct_string_safely(bytes, offset + 3, length - 3, "ISO-8859-13");
                case 0x0E:
                    return construct_string_safely(bytes, offset + 3, length - 3, "ISO-8859-14");
                case 0x0F:
                    return construct_string_safely(bytes, offset + 3, length - 3, "ISO-8859-15");
                default:
                    return construct_string_safely(bytes, offset + 3, length - 3, "UTF-8");
            }
        }

        if (first_byte == 0x11)
            return construct_string_safely(bytes, offset + 1, length - 1, "GBK");

        if (first_byte == 0x13)
            return construct_string_safely(bytes, offset + 1, length - 1, "GB2312");

        if (first_byte == 0x15)
            return construct_string_safely(bytes, offset + 1, length - 1, "UTF-8");

        // 对于其他字符集，按GBK方式解码。
        return new String(bytes, offset, length, Charset.forName("GBK"));
    }

    public static int encodeString(String string, byte[] bytes, int offset)
    {
        if (string == null || string.isEmpty())
            return 0;

        byte[] encoding;
        try
        {
            encoding = string.getBytes("GBK");
        } catch (Exception ex)
        {
            encoding = string.getBytes();
        }

        System.arraycopy(encoding, 0, bytes, offset, encoding.length);
        return encoding.length;
    }

    public static int encodeWithGBKCharset(String string, byte[] bytes, int offset)
    {
        if (string == null || string.isEmpty())
            return 0;

        try
        {
            bytes[offset] = 0x13;
            byte[] encoding = string.getBytes("GBK");
            System.arraycopy(encoding, 0, bytes, offset + 1, encoding.length);
            return encoding.length + 1;
        } catch (Exception ignored)
        {
            // 理论上在中文环境下不会抛出此异常。
            bytes[offset] = 0x13;
            byte[] encoding = string.getBytes(StandardCharsets.ISO_8859_1);
            System.arraycopy(encoding, 0, bytes, offset + 1, encoding.length);
            return encoding.length + 1;
        }
    }

    public static int encodeInASCII(String string, byte[] bytes, int offset)
    {
        if (string == null || string.isEmpty())
            return 0;

        byte[] encoding = string.getBytes(StandardCharsets.US_ASCII);
        if (encoding.length + offset > bytes.length)
            throw new IllegalArgumentException("string too long");

        System.arraycopy(encoding, 0, bytes, offset, encoding.length);
        return encoding.length;
    }

    public static int encodeInGBK(String string, byte[] bytes, int offset)
    {
        if (string == null || string.isEmpty())
            return 0;

        try
        {
            bytes[offset] = 0x11; // GBK 等同于 ISO 10646
            byte[] encoding = string.getBytes("GBK");
            if (encoding.length + 1 + offset > bytes.length)
                throw new IllegalArgumentException("string too long");

            System.arraycopy(encoding, 0, bytes, offset + 1, encoding.length);
            return encoding.length + 1;
        } catch (UnsupportedEncodingException ex)
        {
            // 理论上在中文环境下不会抛出此异常。
            return encodeInUTF8(string, bytes, offset);
        }
    }

    public static int encodeInGB2312(String string, byte[] bytes, int offset)
    {
        if (string == null || string.isEmpty())
            return 0;

        try
        {
            bytes[offset] = 0x13;
            byte[] encoding = string.getBytes("GB2312");
            if (encoding.length + 1 + offset > bytes.length)
                throw new IllegalArgumentException("string too long");

            System.arraycopy(encoding, 0, bytes, offset + 1, encoding.length);
            return encoding.length + 1;
        } catch (UnsupportedEncodingException ex)
        {
            // 理论上在中文环境下不会抛出此异常。
            return encodeInUTF8(string, bytes, offset);
        }
    }

    public static int encodeInBig5(String string, byte[] bytes, int offset)
    {
        if (string == null || string.isEmpty())
            return 0;

        try
        {
            bytes[offset] = 0x14;
            byte[] encoding = string.getBytes("Big5");
            if (encoding.length + 1 + offset > bytes.length)
                throw new IllegalArgumentException("string too long");

            System.arraycopy(encoding, 0, bytes, offset + 1, encoding.length);
            return encoding.length + 1;
        } catch (UnsupportedEncodingException ex)
        {
            // 理论上在中文环境下不会抛出此异常。
            return encodeInUTF8(string, bytes, offset);
        }
    }

    public static int encodeInUTF8(String string, byte[] bytes, int offset)
    {
        if (string == null || string.isEmpty())
            return 0;

        bytes[offset] = 0x15;
        byte[] encoding = string.getBytes(StandardCharsets.UTF_8);
        if (encoding.length + 1 + offset > bytes.length)
            throw new IllegalArgumentException("string too long");

        System.arraycopy(encoding, 0, bytes, offset + 1, encoding.length);
        return encoding.length + 1;
    }

    private static String construct_string_safely(byte[] bytes, int offset, int length, String charset)
    {
        try
        {
            return new String(bytes, offset, length, Charset.forName(charset));
        } catch (Exception ex)
        {
            return new String(bytes, offset, length);
        }
    }
}
