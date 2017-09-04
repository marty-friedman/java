package com.egrasoft.graphalgo.tools;

import com.egrasoft.graphalgo.exceptions.WrongAccessException;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * Util class providing methods to iterate through the collection of element. Collection
 * change methods are not supportable.
 * @param <T> elements' type
 */
public class SafeCollectionWrapper<T> implements Iterable<T> {


    /*==========================Fields==========================*/


    /**
     * Iterable collection.
     */
    private Collection<T> collection;

    /**
     * Collection read-only iterator.
     */
    private SafeIterator it;


    /*=======================Constructors=======================*/


    /**
     * Class constructor. Initializes collection iterator.
     * @param collection collection object
     */
    public SafeCollectionWrapper(Collection<T> collection){
        this.collection = collection;
        it = new SafeIterator();
    }


    /*======================Public Methods======================*/


    @Override
    public Iterator<T> iterator() {
        return it;
    }


    /*======================Nested Classes======================*/


    private class SafeIterator implements Iterator<T>{

        private int position = 0;

        @Override
        public boolean hasNext() {
            return collection.size() > position;
        }

        @Override
        public T next() {
            return collection.;
        }

        @Override
        public void remove() {
            throw new WrongAccessException("Collection modification is not allowed here");
        }

        @Override
        public void forEachRemaining(Consumer<? super T> action) {
            Objects.requireNonNull(action);
            while (hasNext())
                action.accept(next());
        }
    }
}
