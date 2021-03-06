package plantopia.sungshin.plantopia.Plant;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import plantopia.sungshin.plantopia.R;

public class PlantRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerPlantViewHolder> {
    private ArrayList<PlantItem> arrayList;
    private Context mContext;

    public PlantRecyclerViewAdapter(ArrayList<PlantItem> arrayList, Context mContext) {
        this.arrayList = arrayList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public RecyclerPlantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.plant_item, parent, false);
        RecyclerPlantViewHolder listHolder = new RecyclerPlantViewHolder(viewGroup);

        return listHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerPlantViewHolder holder, int position) {
        RecyclerPlantViewHolder mainHolder = (RecyclerPlantViewHolder) holder;
        PlantItem item = arrayList.get(position);

        mainHolder.plantNameText.setText(item.getPlant_name());
        Glide.with(mContext).load(item.getPlant_img()).into(mainHolder.plantImg);
    }

    @Override
    public int getItemCount() {
        return (arrayList != null) ? arrayList.size() : 0;
    }

    public PlantItem getItem(int position) {
        return arrayList.get(position);
    }

    public void clear() {
        arrayList.clear();
    }

    public void addItem(PlantItem item) {
        arrayList.add(item);
    }

}
