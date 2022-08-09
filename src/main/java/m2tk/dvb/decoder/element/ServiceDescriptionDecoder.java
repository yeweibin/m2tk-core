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

package m2tk.dvb.decoder.element;

import m2tk.encoding.Decoder;
import m2tk.encoding.Encoding;

public class ServiceDescriptionDecoder extends Decoder
{
    protected ServiceDescriptionDecoder()
    {
        super(ServiceDescriptionDecoder.class.getSimpleName());
    }

    public int getServiceID()
    {
        return encoding.readUINT16(0);
    }

    public int getEITScheduleFlag()
    {
        return (encoding.readUINT8(2) >> 1) & 0b1;
    }

    public int getEITPresentFollowingFlag()
    {
        return encoding.readUINT8(2) & 0b1;
    }

    public int getRunningStatus()
    {
        return (encoding.readUINT8(3) >> 5) & 0b111;
    }

    public int getFreeCAMode()
    {
        return (encoding.readUINT8(3) >> 4) & 0b1;
    }

    public Encoding getDescriptorLoop()
    {
        int len = encoding.readUINT16(3) & 0xFFF;
        return encoding.readSelector(5, len);
    }
}
