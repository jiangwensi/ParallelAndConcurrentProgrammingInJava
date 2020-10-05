package edu.coursera.concurrent;

import edu.rice.pcdp.Actor;

import java.util.LinkedList;
import java.util.List;

import static edu.rice.pcdp.PCDP.finish;

/**
 * An actor-based implementation of the Sieve of Eratosthenes.
 * <p>
 * TODO Fill in the empty SieveActorActor actor class below and use it from
 * countPrimes to determin the number of primes <= limit.
 */
public final class SieveActor extends Sieve {

    /**
     * {@inheritDoc}
     * <p>
     * TODO Use the SieveActorActor class to calculate the number of primes <=
     * limit in parallel. You might consider how you can model the Sieve of
     * Eratosthenes as a pipeline of actors, each corresponding to a single
     * prime number.
     */

    public static void main(String[] args) {
        System.out.println(new SieveActor().countPrimes(200000 ));
    }

    @Override
    public int countPrimes(final int limit) {


        List<Integer> primes = new LinkedList<>();
        primes.add(2);
        SieveActorActor actor = new SieveActorActor(2,primes);
        finish(() -> {
            for (int i = 2; i <= limit; i++) {
                actor.send(i);
            }
        });
//        for(int i = 0; i < primes.size();i++){
//            System.out.print(primes.get(i)+" ");
//        }
        return primes.size();
    }

    /**
     * An actor class that helps implement the Sieve of Eratosthenes in
     * parallel.
     */
    public static final class SieveActorActor extends Actor {
        /**
         * Process a single message sent to this actor.
         * <p>
         * TODO complete this method.
         *
         * @param msg Received message
         */
        private int divider;
        private SieveActorActor nextActor;
        private List primes;

        public SieveActorActor(int divider,List primes) {
            this.divider = divider;
            this.primes = primes;
        }

        @Override
        public void process(final Object msg) {
            if (((int) msg) % divider != 0) {
                if (nextActor == null) {
                    primes.add((int) msg);
                    nextActor = new SieveActorActor((int) msg,primes);
                }
                nextActor.send(msg);
            }
        }
    }
}
