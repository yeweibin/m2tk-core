/*
 * Copyright (c) M2TK Project. All rights reserved.
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
package m2tk.dvb.decoder.element;

import m2tk.dvb.DVB;
import m2tk.encoding.Decoder;
import m2tk.encoding.Encoding;

public class LocalTimeOffsetDecoder extends Decoder
{
    private static final int BLOCK_SIZE = 13;

    protected LocalTimeOffsetDecoder()
    {
        super(LocalTimeOffsetDecoder.class.getSimpleName());
    }

    @Override
    public boolean isAttachable(Encoding target)
    {
        return super.isAttachable(target) && target.size() == BLOCK_SIZE;
    }

    public String getCountryCode()
    {
        int countryCode = encoding.readUINT24(0);
        return DVB.decodeThreeLetterCode(countryCode);
    }

    public int getCountryRegionID()
    {
        return (encoding.readUINT8(3) >> 2) & 0b111111;
    }

    public int getLocalTimeOffsetPolarity()
    {
        return encoding.readUINT8(3) & 0b1;
    }

    public int getLocalTimeOffset()
    {
        return encoding.readUINT16(4);
    }

    public long getTimeOfChange()
    {
        return encoding.readUINT40(6);
    }

    public int getNextTimeOfChange()
    {
        return encoding.readUINT16(11);
    }
}
