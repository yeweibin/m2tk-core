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

public class AdaptationFieldDecoder extends Decoder
{
    public AdaptationFieldDecoder()
    {
        super(AdaptationFieldDecoder.class.getSimpleName());
    }

    @Override
    public boolean isAttachable(Encoding target)
    {
        // 注意：Encoding包含length字段。
        return super.isAttachable(target) && target.size() <= 184;
    }

    public boolean isEmpty()
    {
        return encoding.readUINT8(0) == 0;
    }

    public int getDiscontinuityIndicator()
    {
        checkNotEmpty();
        return (encoding.readUINT8(1) >> 7) & 0b1;
    }

    public int getRandomAccessIndicator()
    {
        checkNotEmpty();
        return (encoding.readUINT8(1) >> 6) & 0b1;
    }

    public int getElementaryStreamPriorityIndicator()
    {
        checkNotEmpty();
        return (encoding.readUINT8(1) >> 5) & 0b1;
    }

    public int getProgramClockReferenceFlag()
    {
        checkNotEmpty();
        return (encoding.readUINT8(1) >> 4) & 0b1;
    }

    public int getOriginalProgramClockReferenceFlag()
    {
        checkNotEmpty();
        return (encoding.readUINT8(1) >> 3) & 0b1;
    }

    public int getSplicingPointFlag()
    {
        checkNotEmpty();
        return (encoding.readUINT8(1) >> 2) & 0b1;
    }

    public int getTransportPrivateDataFlag()
    {
        checkNotEmpty();
        return (encoding.readUINT8(1) >> 1) & 0b1;
    }

    public int getAdaptationFieldExtensionFlag()
    {
        checkNotEmpty();
        return encoding.readUINT8(1) & 0b1;
    }

    public Encoding getProgramClockReference()
    {
        checkFlag(getProgramClockReferenceFlag());
        return encoding.readSelector(2, 6);
    }

    public Encoding getOriginalProgramClockReference()
    {
        checkFlag(getOriginalProgramClockReferenceFlag());

        int position = 2;
        if (getProgramClockReferenceFlag() == 1)
            position += 6;

        return encoding.readSelector(position, 6);
    }

    public int getSpliceCountdown()
    {
        checkFlag(getSplicingPointFlag());

        int position = 2;
        if (getProgramClockReferenceFlag() == 1)
            position += 6;
        if (getOriginalProgramClockReferenceFlag() == 1)
            position += 6;

        return encoding.readUINT8(position);
    }

    public Encoding getTransportPrivateData()
    {
        checkFlag(getTransportPrivateDataFlag());

        int position = 2;
        if (getProgramClockReferenceFlag() == 1)
            position += 6;
        if (getOriginalProgramClockReferenceFlag() == 1)
            position += 6;
        if (getSplicingPointFlag() == 1)
            position += 1;

        // without 'length' field
        return encoding.readSelector(position + 1, encoding.readUINT8(position));
    }

    public Encoding getAdaptationFieldExtension()
    {
        checkFlag(getAdaptationFieldExtensionFlag());

        int position = 2;
        if (getProgramClockReferenceFlag() == 1)
            position += 6;
        if (getOriginalProgramClockReferenceFlag() == 1)
            position += 6;
        if (getSplicingPointFlag() == 1)
            position += 1;
        if (getTransportPrivateDataFlag() == 1)
            position += 1 + encoding.readUINT8(position);

        // without 'length' field
        return encoding.readSelector(position + 1, encoding.readUINT8(position));
    }

    private void checkNotEmpty()
    {
        if (isEmpty())
            throw new UnsupportedOperationException("no such field");
    }

    private void checkFlag(int flag)
    {
        if (flag == 0)
            throw new UnsupportedOperationException("no such field");
    }
}
