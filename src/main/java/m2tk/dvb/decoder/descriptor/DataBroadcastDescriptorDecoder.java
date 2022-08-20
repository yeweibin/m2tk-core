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
package m2tk.dvb.decoder.descriptor;

import m2tk.encoding.Encoding;
import m2tk.mpeg2.decoder.DescriptorDecoder;

public class DataBroadcastDescriptorDecoder extends DescriptorDecoder
{
    public DataBroadcastDescriptorDecoder()
    {
        super("DataBroadcastDescriptorDecoder");
    }

    @Override
    public boolean isAttachable(Encoding target)
    {
        return super.isAttachable(target) && target.readUINT8(0) == 0x64;
    }

    public int getDataBroadcastID()
    {
        return encoding.readUINT16(2);
    }

    public int getComponentTag()
    {
        return encoding.readUINT8(4);
    }

    public Encoding getSelector()
    {
        int start = 6;
        int length = encoding.readUINT8(5);
        return encoding.readSelector(start, length);
    }
}
