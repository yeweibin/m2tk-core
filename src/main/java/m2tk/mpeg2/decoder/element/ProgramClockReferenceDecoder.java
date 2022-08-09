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

package m2tk.mpeg2.decoder.element;

import m2tk.encoding.Decoder;
import m2tk.encoding.Encoding;
import m2tk.mpeg2.ProgramClockReference;

public class ProgramClockReferenceDecoder extends Decoder
{
    public ProgramClockReferenceDecoder()
    {
        super(ProgramClockReferenceDecoder.class.getSimpleName());
    }

    @Override
    public boolean isAttachable(Encoding target)
    {
        return (super.isAttachable(target) && target.size() == 6);
    }

    public ProgramClockReference getProgramClockReference()
    {
        long base      = encoding.readUINT40(0) >> 7;
        long extension = encoding.readUINT16(4) & 0x1FF;
        return new ProgramClockReference(base, extension);
    }

    public long getProgramClockReferenceValue()
    {
        long base      = encoding.readUINT40(0) >> 7;
        long extension = encoding.readUINT16(4) & 0x1FF;
        return ProgramClockReference.value(base, extension);
    }
}
