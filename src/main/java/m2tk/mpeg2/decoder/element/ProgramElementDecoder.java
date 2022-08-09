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
import m2tk.mpeg2.MPEG2;

public class ProgramElementDecoder extends Decoder
{
    public ProgramElementDecoder()
    {
        super(ProgramElementDecoder.class.getSimpleName());
    }

    public int getStreamType()
    {
        return encoding.readUINT8(0);
    }

    public int getElementaryPID()
    {
        return encoding.readUINT16(1) & MPEG2.PID_MASK;
    }

    public Encoding getDescriptorLoop()
    {
        return encoding.readSelector(5);
    }
}
