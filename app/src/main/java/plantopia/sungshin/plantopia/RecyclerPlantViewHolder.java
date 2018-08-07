package plantopia.sungshin.plantopia;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

class RecyclerPlantViewHolder extends RecyclerView.ViewHolder {
    TextView plantNameText;
    ImageView plantImg;

    public RecyclerPlantViewHolder(View itemView) {
        super(itemView);

        this.plantNameText = (TextView) itemView.findViewById(R.id.plant_item_text);
        this.plantImg = (ImageView) itemView.findViewById(R.id.plant_item_img);
    }
}