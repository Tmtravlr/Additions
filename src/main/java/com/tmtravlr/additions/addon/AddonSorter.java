package com.tmtravlr.additions.addon;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraftforge.fml.common.DummyModContainer;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModAPIManager;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.toposort.TopologicalSort;
import net.minecraftforge.fml.common.toposort.TopologicalSort.DirectedGraph;
import net.minecraftforge.fml.common.versioning.ArtifactVersion;

import com.google.common.collect.Maps;

/**
 * Sorts the addons based on dependencies. Most of the code adapted from ModSorter.
 * 
 * @author Rebeca Rey (Tmtravlr)
 * @date July 2017
 */
public class AddonSorter {
    private DirectedGraph<AddonInfo> modGraph;

    private AddonInfo beforeAll = new AddonInfo();
    private AddonInfo afterAll = new AddonInfo();
    private AddonInfo before = new AddonInfo();
    private AddonInfo after = new AddonInfo();

    public AddonSorter() {
        buildGraph();
    }

    private void buildGraph() {
    	HashMap<String, AddonInfo> nameLookup = Maps.newHashMap(AddonLoader.ADDONS_NAMED);
    	
        modGraph = new DirectedGraph<AddonInfo>();
        modGraph.addNode(beforeAll);
        modGraph.addNode(before);
        modGraph.addNode(afterAll);
        modGraph.addNode(after);
        modGraph.addEdge(before, after);
        modGraph.addEdge(beforeAll, before);
        modGraph.addEdge(after, afterAll);

        for (AddonInfo addon : AddonLoader.addonsLoaded) {
            modGraph.addNode(addon);
        }

        for (AddonInfo addon : AddonLoader.addonsLoaded) {
            boolean preDepAdded = false;
            boolean postDepAdded = false;

            for (String addonId : addon.addonsRequiredBefore) {
                preDepAdded = true;
                
                if (addonId.equals("*"))
                {
                    // We are "after" everything
                    modGraph.addEdge(addon, afterAll);
                    modGraph.addEdge(after, addon);
                    postDepAdded = true;
                }
                else
                {
                    modGraph.addEdge(before, addon);
                    if (AddonLoader.ADDONS_NAMED.containsKey(addonId)) {
                        modGraph.addEdge(AddonLoader.ADDONS_NAMED.get(addonId), addon);
                    }
                }
            }

            for (String addonId : addon.addonsRequiredAfter)
            {
                postDepAdded = true;

                if (addonId.equals("*"))
                {
                    // We are "before" everything
                    modGraph.addEdge(beforeAll, addon);
                    modGraph.addEdge(addon, before);
                    preDepAdded = true;
                }
                else
                {
                    modGraph.addEdge(addon, after);
                    if (AddonLoader.ADDONS_NAMED.containsKey(addonId)) {
                        modGraph.addEdge(addon, AddonLoader.ADDONS_NAMED.get(addonId));
                    }
                }
            }

            if (!preDepAdded)
            {
                modGraph.addEdge(before, addon);
            }

            if (!postDepAdded)
            {
                modGraph.addEdge(addon, after);
            }
        }
    }

    public List<AddonInfo> sort()
    {
        List<AddonInfo> sortedList = TopologicalSort.topologicalSort(modGraph);
        sortedList.removeAll(Arrays.asList(new AddonInfo[] {beforeAll, before, after, afterAll}));
        return sortedList;
    }
}
