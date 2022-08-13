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
                              long interval)
    {
        return bitrate(first.value(), second.value(), interval);
    }

    public static int bitrate(long first, long second, long interval)
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

        return (int) (interval * MPEG2.TS_PACKET_BIT_SIZE * MPEG2.SYSTEM_CLOCK_FREQUENCY / (second - first));
    }

    public static int intervalMillis(ProgramClockReference first, ProgramClockReference second)
    {
        return intervalMillis(first.value(), second.value());
    }

    public static int intervalMillis(long first, long second)
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

        return (int) ((second - first) * 1000 / MPEG2.SYSTEM_CLOCK_FREQUENCY);
    }


    public static int[] toTimeline(long pcr)
    {
        int[] time = new int[4];
        int sec = (int) (pcr / MPEG2.SYSTEM_CLOCK_FREQUENCY);
        int msc = (int) (pcr * 1000 / MPEG2.SYSTEM_CLOCK_FREQUENCY % 1000);
        time[0] = sec / 3600;
        time[1] = sec % 3600 / 60;
        time[2] = sec % 60;
        time[3] = msc;
        return time;
    }

    public static String toTimelineString(long pcr)
    {
        int sec = (int) (pcr / MPEG2.SYSTEM_CLOCK_FREQUENCY);
        int msc = (int) (pcr * 1000 / MPEG2.SYSTEM_CLOCK_FREQUENCY % 1000);
        int hh = sec / 3600;
        int mm = sec % 3600 / 60;
        int ss = sec % 60;
        return String.format("%dh%dm%ds%03d", hh, mm, ss, msc);
    }

    public long value()
    {
        return pcrBase * 300 + pcrExtension;
    }

    public int[] toTimeline()
    {
        return toTimeline(value());
    }

    public String toTimelineString()
    {
        return toTimelineString(value());
    }
}
