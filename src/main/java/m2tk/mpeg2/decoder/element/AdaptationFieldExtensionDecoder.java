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

package m2tk.mpeg2.decoder.element;

import m2tk.encoding.Decoder;
import m2tk.encoding.Encoding;

public class AdaptationFieldExtensionDecoder extends Decoder
{
    public AdaptationFieldExtensionDecoder()
    {
        super(AdaptationFieldExtensionDecoder.class.getSimpleName());
    }

    @Override
    public boolean isAttachable(Encoding target)
    {
        return super.isAttachable(target) && target.size() > 0;
    }

    public int getLegalTimeWindowFlag()
    {
        return (encoding.readUINT8(0) >> 7) & 0b1;
    }

    public int getPiecewiseRateFlag()
    {
        return (encoding.readUINT8(0) >> 6) & 0b1;
    }

    public int getSeamlessSpliceFlag()
    {
        return (encoding.readUINT8(0) >> 5) & 0b1;
    }

    public int getLegalTimeWindowValidFlag()
    {
        checkFlag(getLegalTimeWindowFlag());
        return (encoding.readUINT8(1) >> 7) & 0b1;
    }

    public int getLegalTimeWindowOffset()
    {
        checkFlag(getLegalTimeWindowFlag());
        return encoding.readUINT16(1) & 0x7FFF;
    }

    public int getPiecewiseRate()
    {
        checkFlag(getPiecewiseRateFlag());

        int position = 1;
        if (getLegalTimeWindowFlag() == 1)
            position += 2;

        return encoding.readUINT24(position) & 0x3FFFFF;
    }

    public int getSpliceType()
    {
        checkFlag(getSeamlessSpliceFlag());

        int position = 1;
        if (getLegalTimeWindowFlag() == 1)
            position += 2;
        if (getPiecewiseRateFlag() == 1)
            position += 3;

        return encoding.readUINT8(position) >> 4;
    }

    public long getDecodingTimeStampNextAccessUnit()
    {
        checkFlag(getSeamlessSpliceFlag());

        int position = 1;
        if (getLegalTimeWindowFlag() == 1)
            position += 2;
        if (getPiecewiseRateFlag() == 1)
            position += 3;

        return encoding.readBits(position, 0x0EFFFEFFFEL);
    }

    private void checkFlag(int flag)
    {
        if (flag == 0)
            throw new UnsupportedOperationException("no such field");
    }
}
