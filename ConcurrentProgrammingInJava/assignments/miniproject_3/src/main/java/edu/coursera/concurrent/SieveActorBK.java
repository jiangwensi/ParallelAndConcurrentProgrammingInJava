package edu.coursera.concurrent;

import edu.rice.pcdp.Actor;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static edu.rice.pcdp.PCDP.finish;

/**
 * An actor-based implementation of the Sieve of Eratosthenes.
 * <p>
 * TODO Fill in the empty SieveActorActor actor class below and use it from
 * countPrimes to determin the number of primes <= limit.
 */
public final class SieveActorBK extends Sieve {
    /**
     * {@inheritDoc}
     * <p>
     * TODO Use the SieveActorActor class to calculate the number of primes <=
     * limit in parallel. You might consider how you can model the Sieve of
     * Eratosthenes as a pipeline of actors, each corresponding to a single
     * prime number.
     */

    private static List<Integer> primes = new LinkedList<>();
    private static List<Integer> nums = new LinkedList<>();

    public static void main(String[] args) {
        System.out.println(new SieveActorBK().countPrimes(10));
    }

    @Override
    public int countPrimes(final int limit) {
        for (int i = 2; i <= limit; i++) {
            nums.add(i);
        }

        while (nums.size() > 0) {
            primes.add(nums.get(0));
            SieveActorActor actor = new SieveActorActor();
            finish(() -> {
                nums.stream().forEach(e -> actor.send(e));
            });
        }

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
        @Override
        public void process(final Object msg) {
            int curPrime = primes.get(primes.size() - 1);
            nums = nums.stream().filter(n -> n % curPrime != 0).collect(Collectors.toList());
        }


    }
}
