package com.pooja.carepack.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.pooja.carepack.R;
import com.pooja.carepack.activities.WebServicesConst;
import com.pooja.carepack.model.ModelEditEmailDeletVaccination;
import com.pooja.carepack.model.ModelMedicalFindingInfo;
import com.pooja.carepack.utils.MyPrefs;
import com.pooja.carepack.volly.LibPostListner;
import com.pooja.carepack.volly.PostLibResponse;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;


public class GalleryImageAdapter extends RecyclerView.Adapter<GalleryImageAdapter.ViewHolder> implements LibPostListner {
    private final MyPrefs prefs;
    private OnAddImageClickListener addImageListener;
    private Context context;
    private ArrayList<ModelMedicalFindingInfo.Images> images = new ArrayList<>();

    public GalleryImageAdapter(Context context, ArrayList<ModelMedicalFindingInfo.Images> images, OnAddImageClickListener addImageListener) {
        this.addImageListener = addImageListener;
        this.context = context;
        this.images = images;
        ModelMedicalFindingInfo.Images img = new ModelMedicalFindingInfo().new Images();
        img.image = "";
        img.isLocal = true;
        prefs = new MyPrefs(context);
        this.images.add(getItemCount(), img);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.row_imagemedical, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public ModelMedicalFindingInfo.Images getItem(int position) {
        return images.get(position);
    }

    @Override
    public int getItemCount() {
        int size = images.size();
        return size;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        try {
            holder.imgGallerydelete.setVisibility(position == getItemCount() - 1 ? View.GONE : View.VISIBLE);
            if (position < images.size()) {
                if (images != null && getItem(position) != null && getItem(position).image != null && getItem(position).image.length() > 0) {
                    if (images.get(position).isLocal)
                        Picasso.with(context).load(new File(getItem(position).image)).into(holder.imgGallery);
                    else
                        Picasso.with(context).load(getItem(position).image).into(holder.imgGallery);
                } else {
                    Picasso.with(context).load(R.drawable.ic_add_image).into(holder.imgGallery);
                }
            } else {
                Picasso.with(context).load(R.drawable.ic_add_image).into(holder.imgGallery);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.imgGallery.setTag(position);
        holder.imgGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addImageListener.onAddImageClick(v);
            }
        });
    }


    public void addData(String imageUriList1) {
        ModelMedicalFindingInfo.Images img = new ModelMedicalFindingInfo().new Images();
        img.image = imageUriList1;
        img.isLocal = true;
        this.images.add(getItemCount() - 1, img);
        notifyDataSetChanged();
    }

    public void removeData(int position) {
        if (!getItem(position).isLocal) {
            HashMap<String, String> hashMap = new HashMap<String, String>();
            hashMap.put("userid", prefs.get(MyPrefs.keys.ID));
            hashMap.put("id", getItem(position).id);
            hashMap.put("device_token", prefs.get(MyPrefs.keys.GCMKEY));
            hashMap.put("action", "delete_finding_image");
            new PostLibResponse(GalleryImageAdapter.this, new ModelEditEmailDeletVaccination(), context, hashMap, WebServicesConst.MEDICAL_FINDING, WebServicesConst.RES.DELETE_MEDICALFINDING_INFO, true, true);
        }
        images.remove(position);
        notifyDataSetChanged();
    }

    @Override
    public void onPostResponseComplete(Object clsGson, int requestCode) {

    }

    @Override
    public void onPostResponseError(String errorMessage, int requestCode) {

    }

    public ArrayList<String> getImageUri() {
        ArrayList<String> img = new ArrayList<>();
        for (int i = 0; i < getItemCount(); i++) {
            if (getItem(i).isLocal)
                img.add(getItem(i).image);
        }
        return img;
    }

    public interface OnAddImageClickListener {
        void onAddImageClick(View v);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgGallery;
        private ImageView imgGallerydelete;

        public ViewHolder(View itemView) {
            super(itemView);
            imgGallery = (ImageView) itemView.findViewById(R.id.row_imagemedical_ivGallery);
            imgGallerydelete = (ImageView) itemView.findViewById(R.id.row_imagemedical_ivGallery_delete);
        }

    }
}