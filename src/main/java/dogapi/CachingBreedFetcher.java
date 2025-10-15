package dogapi;

import java.util.*;

/**
 * This BreedFetcher caches fetch request results to improve performance and
 * lessen the load on the underlying data source. An implementation of BreedFetcher
 * must be provided. The number of calls to the underlying fetcher are recorded.
 *
 * If a call to getSubBreeds produces a BreedNotFoundException, then it is NOT cached
 * in this implementation. The provided tests check for this behaviour.
 *
 * The cache maps the name of a breed to its list of sub breed names.
 */
public class CachingBreedFetcher implements BreedFetcher {
    private int callsMade = 0;
    private BreedFetcher breedFetcher;
    private Map<String, List<String>> cachedBreeds;

    public CachingBreedFetcher(BreedFetcher fetcher) {
        super();
        breedFetcher = fetcher;
        cachedBreeds = new HashMap<>();
    }

    @Override
    public List<String> getSubBreeds(String breed) throws BreedNotFoundException {
        List<String> subBreedList;
        if  (cachedBreeds.containsKey(breed)) {
            return cachedBreeds.get(breed);
        } else {
            callsMade++;
            try {
                subBreedList = breedFetcher.getSubBreeds(breed);
                cachedBreeds.put(breed, subBreedList);
            } catch (BreedFetcher.BreedNotFoundException e) {
                throw new BreedFetcher.BreedNotFoundException("Breed not found");
            }
        }
        return subBreedList;
    }

    public int getCallsMade() {
        return callsMade;
    }
}