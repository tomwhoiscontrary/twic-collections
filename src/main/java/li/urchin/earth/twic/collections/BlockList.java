package li.urchin.earth.twic.collections;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class BlockList<E> extends AbstractList<E> {

    private static final int DEFAULT_CAPACITY = 32;

    private List<Block> blocks;

    public BlockList(int blockCapacity) {
        blocks = new ArrayList<>(Collections.singleton(new Block(blockCapacity)));
    }

    public BlockList() {
        this(DEFAULT_CAPACITY);
    }

    public BlockList(int blockCapacity, Collection<E> elements) {
        this(blockCapacity);
        addAll(elements);
    }

    public BlockList(Collection<E> elements) {
        this(DEFAULT_CAPACITY, elements);
    }

    @Override
    public void add(int index, E element) {
        Finger finger = findForInsert(index);
        finger.block.add(index - finger.offset, element);
    }

    @Override
    public E set(int index, E element) {
        Finger finger = find(index);
        return finger.block.set(index - finger.offset, element);
    }

    @Override
    public E get(int index) {
        Finger finger = find(index);
        return finger.block.get(index - finger.offset);
    }

    @Override
    public E remove(int index) {
        Finger finger = find(index);
        return finger.block.remove(index - finger.offset);
    }

    @Override
    public int size() {
        int size = 0;
        for (Block block : blocks) {
            size += block.size;
        }
        return size;
    }

    private class Finger {
        private final Block block;
        private final int offset;

        private Finger(Block block, int offset) {
            this.block = block;
            this.offset = offset;
        }
    }

    private Finger find(int index) {
        int offset = 0;
        for (Block block : blocks) {
            int limit = offset + block.size;
            if (index >= offset && index < limit) {
                return new Finger(block, offset);
            }
            offset = limit;
        }
        throw new IndexOutOfBoundsException(Integer.toString(index));
    }

    private Finger findForInsert(int index) {
        int offset = 0;
        for (Block block : blocks) {
            int limit = offset + block.size;
            if (index >= offset && index <= limit) {
                return new Finger(block, offset);
            }
            offset = limit;
        }
        throw new IndexOutOfBoundsException(Integer.toString(index));
    }

    private class Block {
        private final E[] elements;
        private int size;

        private Block(int capacity) {
            @SuppressWarnings("unchecked") E[] elements = (E[]) new Object[capacity];
            this.elements = elements;
            this.size = 0;
        }

        private void add(int index, E element) {
            checkIndex(index - 1);
            if (index < size) {
                System.arraycopy(elements, index, elements, index + 1, size - index);
            }
            // TODO split blocks when this is out of bounds
            elements[index] = element;
            ++size;
        }

        private E set(int index, E element) {
            checkIndex(index);
            E old = elements[index];
            elements[index] = element;
            return old;
        }

        private E get(int index) {
            checkIndex(index);
            return elements[index];
        }

        private E remove(int index) {
            checkIndex(index);
            E old = elements[index];
            int lastValidIndex = size - 1;
            if (index < lastValidIndex) {
                System.arraycopy(elements, index + 1, elements, index, lastValidIndex - index);
            }
            elements[lastValidIndex] = null;
            --size;
            // TODO unlink empty blocks
            // TODO collapse blocks under a certain amount full?
            return old;
        }

        private void checkIndex(int index) {
            if (index >= size) throw new IndexOutOfBoundsException(Integer.toString(index));
        }
    }

}
