package utils;

import android.content.Context;

import com.example.cuongdx.frequentpattern.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by cuongdx on 12/15/2016.
 */

public class GenRule {

    public ArrayList<String>listRule=new ArrayList<String>();

    public int[] cloneItemSetMinusOneItem(int[] itemset,
                                          Integer itemToRemove) {
        // create the new itemset
        int[] newItemset = new int[itemset.length - 1];
        int i = 0;
        // for each item in this itemset
        for (int j = 0; j < itemset.length; j++) {
            // copy the item except if it is the item that should be excluded
            if (itemset[j] != itemToRemove) {
                newItemset[i++] = itemset[j];
            }
        }
        return newItemset; // return the copy
    }

    public void run(int[] lk) {
        {
            // create a variable H1 for recursion
            List<int[]> H1_for_recursion = new ArrayList<int[]>();

            // For each itemset "itemsetSize1" of size 1 that is member of lk
            for (int item : lk) {
                int itemsetHm_P_1[] = new int[]{item};

                // make a copy of lk without items from hm_P_1
                int[] itemset_Lk_minus_hm_P_1 = cloneItemSetMinusOneItem(lk,
                        item);

                // Now we will calculate the support and confidence
                // of the rule: itemset_Lk_minus_hm_P_1 ==> hm_P_1
                // int support = calculateSupport(itemset_Lk_minus_hm_P_1); //
                // THIS COULD BE
                // // OPTIMIZED ?
                // double supportAsDouble = (double) support;

                // calculate the confidence of the rule :
                // itemset_Lk_minus_hm_P_1 ==> hm_P_1
                // double conf = lk.getAbsoluteSupport() / supportAsDouble;

                // if the confidence is lower than minconf
                // if(conf < minconf || Double.isInfinite(conf)){
                // continue;
                // }

                // double lift = 0;
                int supportHm_P_1 = 0;
                // if the user is using the minlift threshold, we will need
                // to also calculate the lift of the rule:
                // itemset_Lk_minus_hm_P_1 ==> hm_P_1

                // If we are here, it means that the rule respect the minconf
                // and minlift parameters.
                // Therefore, we output the rule.
                // saveRule(itemset_Lk_minus_hm_P_1, support, itemsetHm_P_1,
                saveRule(itemset_Lk_minus_hm_P_1, itemsetHm_P_1);
                // supportHm_P_1, lk.getAbsoluteSupport(), conf, lift);

                // Then we keep the itemset hm_P_1 to find more rules using this
                // itemset and lk.
                H1_for_recursion.add(itemsetHm_P_1);
                // ================ END OF WHAT I HAVE ADDED
            }
            // Finally, we make a recursive call to continue explores rules that
            // can be made with "lk"
            try {
                apGenrules(lk.length, 1, lk, H1_for_recursion);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public void apGenrules(int k, int m, int[] lk, List<int[]> Hm)
            throws IOException {

        // if the itemset "lk" that is used to generate rules is larger than the
        // size of itemsets in "Hm"
        if (k > m + 1) {
            // Create a list that we will be used to store itemsets for the
            // recursive call
            List<int[]> Hm_plus_1_for_recursion = new ArrayList<int[]>();

            // generate candidates using Hm
            List<int[]> Hm_plus_1 = generateCandidateSizeK(Hm);

            // for each such candidates
            for (int[] hm_P_1 : Hm_plus_1) {

                // We subtract the candidate from the itemset "lk"
                int[] itemset_Lk_minus_hm_P_1 = cloneItemSetMinusAnItemset(lk,
                        hm_P_1);

                // We will now calculate the support of the rule Lk/(hm_P_1) ==>
                // hm_P_1
                // we need it to calculate the confidence

                // if the confidence is not enough than we don't need to
                // consider
                // the rule Lk/(hm_P_1) ==> hm_P_1 anymore so we continue

                // if the user is using the minlift threshold, then we will need
                // to calculate the lift of the
                // rule as well and check if the lift is higher or equal to
                // minlift.

                // The rule has passed the confidence and lift threshold
                // requirements,
                // so we can output it
                saveRule(itemset_Lk_minus_hm_P_1, hm_P_1);
                // supportHm_P_1, lk.getAbsoluteSupport(), conf, lift);

                // if k == m+1, then we cannot explore further rules using Lk
                // since Lk will be too small.
                if (k != m + 1) {
                    Hm_plus_1_for_recursion.add(hm_P_1);
                }

            }
            // recursive call to apGenRules to find more rules using "lk"
            apGenrules(k, m + 1, lk, Hm_plus_1_for_recursion);
        }

    }

    public List<int[]> generateCandidateSizeK(List<int[]> levelK_1) {
        // create a variable to store candidates
        List<int[]> candidates = new ArrayList<int[]>();

        // For each itemset I1 and I2 of level k-1
        loop1:
        for (int i = 0; i < levelK_1.size(); i++) {
            int[] itemset1 = levelK_1.get(i);
            loop2:
            for (int j = i + 1; j < levelK_1.size(); j++) {
                int[] itemset2 = levelK_1.get(j);

                // we compare items of itemset1 and itemset2.
                // If they have all the same k-1 items and the last item of
                // itemset1 is smaller than
                // the last item of itemset2, we will combine them to generate a
                // candidate
                for (int k = 0; k < itemset1.length; k++) {
                    // if they are the last items
                    if (k == itemset1.length - 1) {
                        // the one from itemset1 should be smaller (lexical
                        // order)
                        // and different from the one of itemset2
                        if (itemset1[k] >= itemset2[k]) {
                            continue loop1;
                        }
                    }
                    // if they are not the last items, and
                    else if (itemset1[k] < itemset2[k]) {
                        continue loop2; // we continue searching
                    } else if (itemset1[k] > itemset2[k]) {
                        continue loop1; // we stop searching: because of lexical
                        // order
                    }
                }

                // Create a new candidate by combining itemset1 and itemset2
                int lastItem1 = itemset1[itemset1.length - 1];
                int lastItem2 = itemset2[itemset2.length - 1];
                int newItemset[];
                if (lastItem1 < lastItem2) {
                    // Create a new candidate by combining itemset1 and itemset2
                    newItemset = new int[itemset1.length + 1];
                    System.arraycopy(itemset1, 0, newItemset, 0,
                            itemset1.length);
                    newItemset[itemset1.length] = lastItem2;
                    candidates.add(newItemset);
                } else {
                    // Create a new candidate by combining itemset1 and itemset2
                    newItemset = new int[itemset1.length + 1];
                    System.arraycopy(itemset2, 0, newItemset, 0,
                            itemset2.length);
                    newItemset[itemset2.length] = lastItem1;
                    candidates.add(newItemset);
                }

            }
        }
        // return the set of candidates
        return candidates;
    }

    public int[] cloneItemSetMinusAnItemset(int[] itemset,
                                            int[] itemsetToNotKeep) {
        // create a new itemset
        int[] newItemset = new int[itemset.length - itemsetToNotKeep.length];
        int i = 0;
        // for each item of this itemset
        for (int j = 0; j < itemset.length; j++) {
            // copy the item except if it is not an item that should be excluded
            if (Arrays.binarySearch(itemsetToNotKeep, itemset[j]) < 0) {
                newItemset[i++] = itemset[j];
            }
        }
        return newItemset; // return the copy
    }


    public void saveRule(int[] itemset1, int[] itemset2) {
        StringBuilder buffer = new StringBuilder();
        // write itemset 1
        for (int i = 0; i < itemset1.length; i++) {
            buffer.append(itemset1[i]);
            if (i != itemset1.length - 1) {
                buffer.append(" ");
            }
        }
        // write separator
        buffer.append(" : ");
        // write itemset 2
        for (int i = 0; i < itemset2.length; i++) {
            buffer.append(itemset2[i]);
            if (i != itemset2.length - 1) {
                buffer.append(" ");
            }
        }
        listRule.add(buffer.toString());
    }

    public boolean findRule(Context context) {
        for(String rule:listRule)
        try {
            InputStream is=context.getResources().openRawResource(R.raw.conf);
            BufferedReader bfr = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = bfr.readLine()) != null) {
                if (line.equals(rule))
                    return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public GenRule(int[] rule,Context context) {
        run(rule);

    }

    public void genRule(int []rule){
        StringBuilder sb=new StringBuilder();
        for(int i:rule)
            for(int j:rule)
                if(i!=j){
                    sb.append(i+" : "+j);
                    listRule.add(sb.toString());
                }
    }
}
