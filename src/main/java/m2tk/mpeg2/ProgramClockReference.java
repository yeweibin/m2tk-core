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

package m2tk.mpeg2;

public final class ProgramClockReference
{
    public final long pcrBase;
    public final long pcrExtension;

    public ProgramClockReference(long pcr)
    {
        pcrBase = (pcr / 300) & 0x1FFFFFFFFL;
        pcrExtension = (pcr % 300) & 0x1FFL;
    }

    public ProgramClockReference(long base, long extension)
    {
        pcrBase = base & 0x1FFFFFFFFL;
        pcrExtension = extension & 0x1FFL;
    }

    public static long base(long pcr)
    {
        return (pcr / 300) & 0x1FFFFFFFFL;
    }

    public static long extension(long pcr)
    {
        return (pcr % 300) & 0x1FFL;
    }

    public static long value(long base, long extension)
    {
        return (base & 0x1FFFFFFFFL) * 300 + (extension & 0x1FF);
    }

    public static int bitrate(ProgramClockReference first,
                              ProgramClockReference second,
                              long packets)
    {
        return bitrate(first.value(), second.value(), packets);
    }

    public static int bitrate(long first, long second, long packets)
    {
        if (first == second)
            throw new IllegalArgumentException("invalid pcr value: first(" + first + "), second(" + second + ")");

        if (first > second)
        {
            // PCR wrap round
            long base = ((second / 300) & 0x1FFFFFFFFL) + 0x200000000L;
            long extension = ((second % 300) & 0x1FFL);
            second = base * 300 + extension;
        }

        return (int) (packets * MPEG2.TS_PACKET_BIT_SIZE * MPEG2.SYSTEM_CLOCK_FREQUENCY_HZ / (second - first));
    }

    public static long deltaMillis(ProgramClockReference first,
                                   ProgramClockReference second)
    {
        return deltaMillis(first.value(), second.value());
    }

    public static long deltaMillis(long first, long second)
    {
        if (first == second)
            throw new IllegalArgumentException("invalid pcr value: first(" + first + "), second(" + second + ")");

        if (first > second)
        {
            // PCR wrap round
            long base = ((second / 300) & 0x1FFFFFFFFL) + 0x200000000L;
            long extension = ((second % 300) & 0x1FFL);
            second = base * 300 + extension;
        }

        return (second - first) * 1000 / MPEG2.SYSTEM_CLOCK_FREQUENCY_HZ;
    }

    public static long deltaNanos(ProgramClockReference first,
                                  ProgramClockReference second)
    {
        return deltaNanos(first.value(), second.value());
    }

    public static long deltaNanos(long first, long second)
    {
        if (first == second)
            throw new IllegalArgumentException("invalid pcr value: first(" + first + "), second(" + second + ")");

        if (first > second)
        {
            // PCR wrap round
            long base = ((second / 300) & 0x1FFFFFFFFL) + 0x200000000L;
            long extension = ((second % 300) & 0x1FFL);
            second = base * 300 + extension;
        }

        return (second - first) * 1000 / MPEG2.SYSTEM_CLOCK_FREQUENCY_MHZ;
    }

    public static double toTimepoint(long pcr)
    {
        return 1.0 * pcr / MPEG2.SYSTEM_CLOCK_FREQUENCY_HZ;
    }

    public static String toTimepointString(long pcr)
    {
        int sec = (int) (pcr / MPEG2.SYSTEM_CLOCK_FREQUENCY_HZ);
        int msc = (int) (pcr * 1000 / MPEG2.SYSTEM_CLOCK_FREQUENCY_HZ % 1000);
        int dd = sec / 86400;
        int hh = sec % 86400 / 3600;
        int mm = sec % 3600 / 60;
        int ss = sec % 60;
        return String.format("%02d:%02d:%02d:%02d.%03d", dd, hh, mm, ss, msc);
    }

    public long value()
    {
        return pcrBase * 300 + pcrExtension;
    }

    public double toTimepoint()
    {
        return toTimepoint(value());
    }

    public String toTimepointString()
    {
        return toTimepointString(value());
    }
}
