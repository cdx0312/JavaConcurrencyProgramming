package third;

import java.util.concurrent.atomic.AtomicReferenceArray;

public class LockFreeVector<E> {
    private final AtomicReferenceArray<AtomicReferenceArray<E>> buckets;

    public LockFreeVector() {
        buckets = new AtomicReferenceArray<AtomicReferenceArray<E>>(30);
        buckets.set(0, new AtomicReferenceArray<E>(8));

    }

    static class Descriptor<E> {
        public int size;
        volatile WriteDescriptor<E> writeop;

        public Descriptor(int size, WriteDescriptor<E> writeop) {
            this.size = size;
            this.writeop = writeop;
        }

        public void completeWrite() {
            WriteDescriptor<E> tmpOp = writeop;
            if (tmpOp != null) {
                tmpOp.doIt();
                writeop = null;
            }
        }
    }

    static class WriteDescriptor<E> {
        public E oldV;
        public E newV;
        public AtomicReferenceArray<E> addr;
        public int addr_ind;

        public WriteDescriptor(E oldV, E newV, AtomicReferenceArray<E> addr, int addr_ind) {
            this.oldV = oldV;
            this.newV = newV;
            this.addr = addr;
            this.addr_ind = addr_ind;
        }

        public void doIt() {
            addr.compareAndSet(addr_ind, oldV, newV);
        }
    }
}
