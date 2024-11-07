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

import m2tk.encoding.Decoder;
import m2tk.encoding.Encoding;

public class EventDescriptionDecoder extends Decoder
{
    public EventDescriptionDecoder()
    {
        super(EventDescriptionDecoder.class.getSimpleName());
    }

    public int getEventID()
    {
        return encoding.readUINT16(0);
    }

    public long getStartTime()
    {
        return encoding.readUINT40(2);
    }

    public int getDuration()
    {
        return encoding.readUINT24(7);
    }

    public int getRunningStatus()
    {
        return (encoding.readUINT8(10) >> 5) & 0b111;
    }

    public int getFreeCAMode()
    {
        return (encoding.readUINT8(10) >> 4) & 0b1;
    }

    public Encoding getDescriptorLoop()
    {
        int len = encoding.readUINT16(10) & 0xFFF;
        return encoding.readSelector(12, len);
    }
}
