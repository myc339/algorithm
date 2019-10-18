package info6205.assignment5;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

/**
 * This code has been fleshed out by Ziyao Qiao. Thanks very much.
 * TODO tidy it up a bit.
 */

class ParSort {

    public static int cutoff = 1000;

    public static void sort(int[] array, int from, int to,Executor threads) {
        if (to - from < cutoff) Arrays.sort(array, from, to);
        else {
            CompletableFuture<int[]> parsort1 = parsort(array, from, from + (to - from) / 2,threads); // TO IMPLEMENT
            CompletableFuture<int[]> parsort2 = parsort(array, from + (to - from) / 2, to,threads); // TO IMPLEMENT
            CompletableFuture<int[]> parsort = parsort1.thenCombine(parsort2, (xs1, xs2) -> {
                        int[] result = new int[xs1.length + xs2.length];
                        // TO BE IMPLEMENTED ...
	                    int i=0,j=0;
	                    for(int k=0;k<result.length;k++)
	                    {	
	                    	if(i>=xs1.length)
	                    	{
	                    		result[k]=xs2[j++];
	                    	}
	                    	else if(j>=xs2.length)
	                    	{
	                    		result[k]=xs1[i++];
	                    	}
	                    	else if(xs1[i]>xs2[j])
	                    	{
	                    		result[k]=xs2[j++];
	                    	}
	                    	else 
	                    	{
	                    		result[k]=xs1[i++];
	                    	}     	
	                    	
	                    }
                        // ... END IMPLEMENTATION
                        return result;               
                    });

            parsort.whenComplete((result, throwable) -> System.arraycopy(result, 0, array, from, result.length));
//            System.out.println("# threads: "+ ForkJoinPool.commonPool().getRunningThreadCount());
            parsort.join();
        }
    }

    private static CompletableFuture<int[]> parsort(int[] array, int from, int to,Executor threads) {
        return CompletableFuture.supplyAsync(
                () -> {
                	
                    int[] result = new int[to - from];
                    // TO BE IMPLEMENTED ...
                    System.arraycopy(array, from, result, 0, result.length);
                    sort(result,0,to-from,threads);
                    // ... END IMPLEMENTATION
                    return result;
                }
        ,threads);
    }
}