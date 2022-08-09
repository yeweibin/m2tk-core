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

package m2tk.mpeg2.decoder.section;

import m2tk.encoding.Encoding;
import m2tk.mpeg2.MPEG2;
import m2tk.mpeg2.decoder.PSISectionDecoder;
import m2tk.util.IntBiConsumer;

import java.util.Arrays;
import java.util.Objects;

public class PATSectionDecoder extends PSISectionDecoder
{
    public PATSectionDecoder()
    {
        super(PATSectionDecoder.class.getSimpleName());
    }

    @Override
    public boolean isAttachable(Encoding target)
    {
        return (super.isAttachable(target) && target.readUINT8(0) == 0x00);
    }

    public int getTransportStreamID()
    {
        return getTableIDExtension();
    }

    public boolean containsNetworkInformationPID()
    {
        return getNetworkInformationPID() != MPEG2.INVALID_PID;
    }

    public int getNetworkInformationPID()
    {
        int offset = 8;
        int finish = encoding.size() - 4;
        while (offset + 4 <= finish)
        {
            if (encoding.readUINT16(offset) == 0)
                return encoding.readUINT16(offset + 2) & MPEG2.PID_MASK;
            offset += 4;
        }
        return MPEG2.INVALID_PID;
    }

    /*
     * 此列表与PID列表保持相同的顺序，即相同位置上的值构成一组关联关系。
     */
    public int[] getProgramNumberList()
    {
        int i = 0;
        int offset = 8;
        int finish = encoding.size() - 4;
        int[] list = new int[(finish - offset) / 4];
        while (offset + 4 <= finish)
        {
            int program = encoding.readUINT16(offset);
            if (program != 0)
            {
                list[i] = program;
                i ++;
            }
            offset += 4;
        }
        return Arrays.copyOf(list, i);
    }

    /*
     * 此列表与ProgramNumber列表保持相同的顺序，即相同位置上的值构成一组关联关系。
     */
    public int[] getProgramMapPIDList()
    {
        int i = 0;
        int offset = 8;
        int finish = encoding.size() - 4;
        int[] list = new int[(finish - offset) / 4];
        while (offset + 4 <= finish)
        {
            int program = encoding.readUINT16(offset);
            if (program != 0)
            {
                list[i] = encoding.readUINT16(offset + 2) & MPEG2.PID_MASK;
                i ++;
            }
            offset += 4;
        }
        return Arrays.copyOf(list, i);
    }

    public void forEachProgramAssociation(IntBiConsumer consumer)
    {
        Objects.requireNonNull(consumer);
        int offset = 8;
        int finish = encoding.size() - 4;
        while (offset + 4 <= finish)
        {
            int program = encoding.readUINT16(offset);
            int pmtpid = encoding.readUINT16(offset + 2) & MPEG2.PID_MASK;
            if (program != 0)
                consumer.accept(program, pmtpid);
            offset += 4;
        }
    }
}
