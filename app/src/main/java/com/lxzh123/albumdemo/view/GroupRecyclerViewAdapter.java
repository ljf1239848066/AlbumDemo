package com.lxzh123.albumdemo.view;

/**
 * description
 * author      Created by lxzh
 * date        2018/8/23
 */

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

/**
 * An extension to RecyclerView.Adapter to provide sections with headers and footers to a
 * RecyclerView. Each section can have an arbitrary number of items.
 *
 * @param <H> Class extending RecyclerView.ViewHolder to hold and bind the header view
 * @param <VH> Class extending RecyclerView.ViewHolder to hold and bind the items view
 */
public abstract class GroupRecyclerViewAdapter<H extends RecyclerView.ViewHolder,
        VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    protected static final int TYPE_SECTION_HEADER = -1;
    protected static final int TYPE_ITEM = -3;

    private int[] sectionForPosition = null;
    private int[] positionWithinSection = null;
    private boolean[] isHeader = null;
    private int count = 0;

    public GroupRecyclerViewAdapter() {
        super();
        registerAdapterDataObserver(new SectionDataObserver());
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        setupIndices();
    }

    /**
     * Returns the sum of number of items for each section plus headers and footers if they
     * are provided.
     */
    @Override
    public int getItemCount() {
        return count;
    }

    private void setupIndices(){
        count = countItems();
        allocateAuxiliaryArrays(count);
        precomputeIndices();
    }

    private int countItems() {
        int count = 0;
        int sections = getSectionCount();

        for(int i = 0; i < sections; i++){
            count += 1 + getItemCountForSection(i);
        }
        return count;
    }

    private void precomputeIndices(){
        int sections = getSectionCount();
        int index = 0;

        for(int i = 0; i < sections; i++){
            setPrecomputedItem(index, true, false, i, 0);
            index++;

            for(int j = 0; j < getItemCountForSection(i); j++){
                setPrecomputedItem(index, false, false, i, j);
                index++;
            }
        }
    }

    private void allocateAuxiliaryArrays(int count) {
        sectionForPosition = new int[count];
        positionWithinSection = new int[count];
        isHeader = new boolean[count];
    }

    private void setPrecomputedItem(int index, boolean isHeader, boolean isFooter, int section, int position) {
        this.isHeader[index] = isHeader;
        sectionForPosition[index] = section;
        positionWithinSection[index] = position;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;

        if(isSectionHeaderViewType(viewType)){
            viewHolder = onCreateSectionHeaderViewHolder(parent, viewType);
        }else{
            viewHolder = onCreateItemViewHolder(parent, viewType);
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int section = sectionForPosition[position];
        int index = positionWithinSection[position];

        if(isSectionHeaderPosition(position)){
            onBindSectionHeaderViewHolder((H) holder, section);
        }else{
            onBindItemViewHolder((VH) holder, section, index);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, @NonNull List<Object> payloads) {
        int section = sectionForPosition[position];
        int index = positionWithinSection[position];

        if(payloads.isEmpty()){
            onBindViewHolder(holder,position);
        }else{
            if(!isSectionHeaderPosition(position)){
                onBindItemViewHolderEx((VH) holder, section, index);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {

        if(sectionForPosition == null){
            setupIndices();
        }

        int section = sectionForPosition[position];
        int index = positionWithinSection[position];

        if(isSectionHeaderPosition(position)){
            return getSectionHeaderViewType(section);
        }else{
            return getSectionItemViewType(section, index);
        }

    }

    protected int getSectionHeaderViewType(int section){
        return TYPE_SECTION_HEADER;
    }

    protected int getSectionItemViewType(int section, int position){
        return TYPE_ITEM;
    }

    /**
     * Returns true if the argument position corresponds to a header
     */
    public boolean isSectionHeaderPosition(int position){
        if(isHeader == null){
            setupIndices();
        }
        return isHeader[position];
    }

    protected boolean isSectionHeaderViewType(int viewType){
        return viewType == TYPE_SECTION_HEADER;
    }

    /**
     * Returns the number of sections in the RecyclerView
     */
    protected abstract int getSectionCount();

    /**
     * Returns the number of items for a given section
     */
    protected abstract int getItemCountForSection(int section);

    /**
     * Creates a ViewHolder of class H for a Header
     */
    protected abstract H  onCreateSectionHeaderViewHolder(ViewGroup parent, int viewType);

    /**
     * Creates a ViewHolder of class VH for an Item
     */
    protected abstract VH onCreateItemViewHolder(ViewGroup parent, int viewType);

    /**
     * Binds data to the header view of a given section
     */
    protected abstract void onBindSectionHeaderViewHolder(H holder, int section);

    /**
     * Binds data to the item view for a given position within a section
     */
    protected abstract void onBindItemViewHolder(VH holder, int section, int position);
    /**
     * Binds data to the item view for a given position within a section
     */
    protected abstract void onBindItemViewHolderEx(VH holder, int section, int position);

    class SectionDataObserver extends RecyclerView.AdapterDataObserver{
        @Override
        public void onChanged() {
            setupIndices();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            setupIndices();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            setupIndices();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            setupIndices();
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            setupIndices();
        }
    }
}

