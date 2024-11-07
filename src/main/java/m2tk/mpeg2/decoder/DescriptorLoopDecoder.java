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

package m2tk.mpeg2.decoder;

import m2tk.encoding.Decoder;
import m2tk.encoding.Encoding;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class DescriptorLoopDecoder extends Decoder
{
    public DescriptorLoopDecoder()
    {
        super(DescriptorLoopDecoder.class.getSimpleName());
    }

    public int getDescriptorCount()
    {
        int from = 0;
        int to = encoding.size();
        int count = 0;
        while (from < to)
        {
            count++;
            from += 2 + encoding.readUINT8(from + 1);
        }
        return count;
    }

    public int getDescriptorCount(Predicate<Encoding> predicate)
    {
        Objects.requireNonNull(predicate);
        int from = 0;
        int to = encoding.size();
        int count = 0;
        while (from < to)
        {
            int length = 2 + encoding.readUINT8(from + 1);
            Encoding descriptor = encoding.readSelector(from, length);
            if (predicate.test(descriptor))
                count ++;
            from += length;
        }
        return count;
    }

    public Encoding[] getDescriptorList()
    {
        ArrayList<Encoding> list = new ArrayList<>();
        int from = 0;
        int to = encoding.size();
        while (from < to)
        {
            int length = 2 + encoding.readUINT8(from + 1);
            list.add(encoding.readSelector(from, length));
            from += length;
        }
        return list.toArray(new Encoding[0]);
    }

    public Encoding[] getDescriptorList(Predicate<Encoding> predicate)
    {
        Objects.requireNonNull(predicate);
        ArrayList<Encoding> list = new ArrayList<>();
        int from = 0;
        int to = encoding.size();
        while (from < to)
        {
            int length = 2 + encoding.readUINT8(from + 1);
            Encoding descriptor = encoding.readSelector(from, length);
            if (predicate.test(descriptor))
                list.add(descriptor);
            from += length;
        }
        return list.toArray(new Encoding[0]);
    }

    public void forEach(Consumer<Encoding> consumer)
    {
        Objects.requireNonNull(consumer);
        int from = 0;
        int to = encoding.size();
        while (from < to)
        {
            int length = 2 + encoding.readUINT8(from + 1);
            Encoding descriptor = encoding.readSelector(from, length);
            consumer.accept(descriptor);
            from += length;
        }
    }

    public void forEach(Predicate<Encoding> predicate, Consumer<Encoding> consumer)
    {
        Objects.requireNonNull(predicate);
        Objects.requireNonNull(consumer);
        int from = 0;
        int to = encoding.size();
        while (from < to)
        {
            int length = 2 + encoding.readUINT8(from + 1);
            Encoding descriptor = encoding.readSelector(from, length);
            if (predicate.test(descriptor))
                consumer.accept(descriptor);
            from += length;
        }
    }

    public void applyFirst(Predicate<Encoding> predicate, Consumer<Encoding> consumer)
    {
        Objects.requireNonNull(predicate);
        Objects.requireNonNull(consumer);
        int from = 0;
        int to = encoding.size();
        while (from < to)
        {
            int length = 2 + encoding.readUINT8(from + 1);
            Encoding descriptor = encoding.readSelector(from, length);
            if (predicate.test(descriptor))
            {
                consumer.accept(descriptor);
                return;
            }
            from += length; // continue till find (first) one
        }
    }

    public Optional<Encoding> findFirstDescriptor(int tag)
    {
        int from = 0;
        int to = encoding.size();
        while (from < to)
        {
            int length = 2 + encoding.readUINT8(from + 1);
            if (encoding.readUINT8(from) == tag)
                return Optional.of(encoding.readSelector(from, length));
            from += length;
        }
        return Optional.empty();
    }

    public Optional<Encoding> findFirstDescriptor(Predicate<Encoding> predicate)
    {
        int from = 0;
        int to = encoding.size();
        while (from < to)
        {
            int length = 2 + encoding.readUINT8(from + 1);
            Encoding descriptor = encoding.readSelector(from, length);
            if (predicate.test(descriptor))
                return Optional.of(descriptor);
            from += length;
        }
        return Optional.empty();
    }

    public Optional<Encoding[]> findAllDescriptors(Predicate<Encoding> predicate)
    {
        ArrayList<Encoding> list = new ArrayList<>();
        int from = 0;
        int to = encoding.size();
        while (from < to)
        {
            int length = 2 + encoding.readUINT8(from + 1);
            Encoding descriptor = encoding.readSelector(from, length);
            if (predicate.test(descriptor))
                list.add(descriptor);
            from += length;
        }
        return list.isEmpty()
               ? Optional.empty()
               : Optional.of(list.toArray(new Encoding[0]));
    }
}
