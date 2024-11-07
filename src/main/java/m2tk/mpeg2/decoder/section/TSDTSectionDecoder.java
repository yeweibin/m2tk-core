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

package m2tk.mpeg2.decoder.section;

import m2tk.encoding.Encoding;
import m2tk.mpeg2.decoder.PSISectionDecoder;

public class TSDTSectionDecoder extends PSISectionDecoder
{
    public TSDTSectionDecoder()
    {
        super(TSDTSectionDecoder.class.getSimpleName());
    }

    @Override
    public boolean isAttachable(Encoding target)
    {
        return (super.isAttachable(target) && target.readUINT8(0) == 0x03);
    }

    public Encoding getDescriptorLoop()
    {
        return encoding.readSelector(8, encoding.size() - 12);
    }
}
