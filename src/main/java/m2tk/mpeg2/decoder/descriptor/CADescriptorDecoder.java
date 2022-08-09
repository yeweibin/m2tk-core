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

package m2tk.mpeg2.decoder.descriptor;

import m2tk.encoding.Encoding;
import m2tk.mpeg2.MPEG2;
import m2tk.mpeg2.decoder.DescriptorDecoder;

public class CADescriptorDecoder extends DescriptorDecoder
{
    public CADescriptorDecoder()
    {
        super(CADescriptorDecoder.class.getSimpleName());
    }

    @Override
    public boolean isAttachable(Encoding target)
    {
        return (super.isAttachable(target) && target.readUINT8(0) == 0x09);
    }

    public int getConditionalAccessSystemID()
    {
        return encoding.readUINT16(2);
    }

    public int getConditionalAccessStreamPID()
    {
        return encoding.readUINT16(4) & MPEG2.PID_MASK;
    }

    public Encoding getPrivateData()
    {
        return encoding.readSelector(6);
    }

    public byte[] getPrivateDataBytes()
    {
        return encoding.getRange(6, encoding.size());
    }
}
