package com.traidev.mcfresh.Category.Shops.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.traidev.mcfresh.R;
import com.traidev.mcfresh.Utility.DefaultResponse;
import com.traidev.mcfresh.Utility.Network.RetrofitClient;
import com.traidev.mcfresh.Utility.SharedPrefManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.traidev.mcfresh.Category.Shops.ShopPageActivity.OFFCHECK;
import static com.traidev.mcfresh.Utility.Network.RetrofitClient.BASE_URL;

public class ResycleRestaurants extends RecyclerView.Adapter {

    private List<ModalOffers> modalOffersList;
    private Context context;
    private Button checkBtn;
    int CHECKREQUEST = 0;
    ImageView plus, minus;
    int fam=0;

    public ResycleRestaurants(List<ModalOffers> modalOffersList, Context context, Button checkbtn) {
        this.modalOffersList = modalOffersList;
        this.context = context;
        this.checkBtn = checkbtn;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View view;

        if(viewType == ModalOffers.TYPE_1)
        {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.modal_restaurant_cat,viewGroup,false);
            return new ModalFirst(view);
        }
        else if(viewType == ModalOffers.TYPE_2)
        {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.modal_cart_shop_layout,viewGroup,false);
            return new ModalSecond(view);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {

        ModalOffers object = modalOffersList.get(position);

        switch (object.getProType())
        {
            case ModalOffers.TYPE_1:
                ((ModalFirst) holder).product.setText(modalOffersList.get(position).getPro());
                ((ModalFirst) holder).desc.setText(modalOffersList.get(position).getQty()+" Items");
                break;
            case ModalOffers.TYPE_2:
                ((ModalSecond) holder).product.setText(modalOffersList.get(position).getPro());
                ((ModalSecond) holder).desc.setText(modalOffersList.get(position).getDesc());

                if(modalOffersList.get(position).getBanner().length()>4) {
                    Glide.with(context).load(BASE_URL + "con/product/" + modalOffersList.get(position).getBanner()).dontAnimate().diskCacheStrategy(DiskCacheStrategy.ALL).into(((ModalSecond) holder).proThumb);
                }
                else
                {
                    ((ModalSecond) holder).proThumb.setVisibility(View.INVISIBLE);
                    ((ModalSecond) holder).proThumb.getLayoutParams().width =390;
                }

                if(modalOffersList.get(position).getDisc().equals("1"))
                {
                    if(modalOffersList.get(position).getVariation().equals("null"))
                     ((ModalSecond) holder).product.setCompoundDrawablesWithIntrinsicBounds(R.drawable.side_red, 0, 0, 0);
                }

                final int amount = Integer.parseInt(modalOffersList.get(position).getPrice());


                final String Id = modalOffersList.get(position).getId();
                final String ShopId = modalOffersList.get(position).getShopID();

                ((ModalSecond) holder).price.setText("\u20B9" + amount);
                ((ModalSecond) holder).price.setHint(String.valueOf(amount));

                if (modalOffersList.get(position).getQty() > 0) {
                    ((ModalSecond) holder).Box.setVisibility(View.VISIBLE);
                    ((ModalSecond) holder).CartClick.setVisibility(View.GONE);
                    ((ModalSecond) holder).qtyUpdate.setText(String.valueOf(modalOffersList.get(position).getQty()));
                    ((ModalSecond) holder).priceUpdate.setText("\u20B9 " + amount * modalOffersList.get(position).getQty());
                    checkBtn.setVisibility(View.VISIBLE);
                } else {
                    ((ModalSecond) holder).priceUpdate.setText("\u20B9 " + amount);
                }


                if (OFFCHECK != 1) {
                    ((ModalSecond) holder).CartClick.setClickable(false);
                    ((ModalSecond) holder).CartClick.setEnabled(false);
                    ((ModalSecond) holder).Box.setVisibility(View.GONE);
                    ((ModalSecond) holder).CartClick.setVisibility(View.VISIBLE);
                    ((ModalSecond) holder).CartClick.setBackgroundTintList(context.getResources().getColorStateList(R.color.adddisalbe));
                }


                ((ModalSecond) holder).CartClick.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(modalOffersList.get(position).getVariation().equals("null"))
                        {
                            AddtoCart(Id, ShopId, "1",modalOffersList.get(position).getPrice());
                            ((ModalSecond) holder).Box.setVisibility(View.VISIBLE);
                            ((ModalSecond) holder).CartClick.setVisibility(View.GONE);
                            checkBtn.setVisibility(View.VISIBLE);
                            ((ModalSecond) holder).qtyUpdate.setText("1");
                        }
                        else
                        {
                            String type = modalOffersList.get(position).getVariation();
                            String title = modalOffersList.get(position).getPro();
                            TextView p1,p2,p3;
                            RadioButton r1,r2,r3;

                            String[] dif = type.split("#");

                            final BottomSheetDialog filter = new BottomSheetDialog(v.getContext());

                            if(dif[0].equals("1"))
                            {
                                filter.setContentView(R.layout.variation_modal_weight);
                                p1 = filter.findViewById(R.id.p1);
                                p2 = filter.findViewById(R.id.p2);
                                p3 = filter.findViewById(R.id.p3);
                                r1 = filter.findViewById(R.id.q1);
                                r2 = filter.findViewById(R.id.q2);
                                r3 = filter.findViewById(R.id.q3);

                                if(!dif[1].equals("0"))
                                {   p1.append(dif[1]);}
                                else
                                {  p1.setVisibility(View.GONE); r1.setVisibility(View.GONE); }
                                if(!dif[2].equals("0"))
                                    p2.append(dif[2]);
                                else
                                {  p2.setVisibility(View.GONE); r2.setVisibility(View.GONE);}

                                if(!dif[3].equals("0"))
                                    p3.append(dif[3]);
                                else
                                {  p3.setVisibility(View.GONE); r3.setVisibility(View.GONE);}
                            }
                            else if(dif[0].equals("2"))
                            {
                                filter.setContentView(R.layout.variation_modal_size);
                                p1 = filter.findViewById(R.id.p1);
                                p2 = filter.findViewById(R.id.p2);
                                p3 = filter.findViewById(R.id.p3);
                                r1 = filter.findViewById(R.id.q1);
                                r2 = filter.findViewById(R.id.q2);
                                r3 = filter.findViewById(R.id.q3);
                                if(!dif[1].equals("0"))
                                {   p1.append(dif[1]);}
                                else
                                {  p1.setVisibility(View.GONE); r1.setVisibility(View.GONE); }
                                if(!dif[2].equals("0"))
                                    p2.append(dif[2]);
                                else
                                {  p2.setVisibility(View.GONE); r2.setVisibility(View.GONE);}

                                if(!dif[3].equals("0"))
                                    p3.append(dif[3]);
                                else
                                {  p3.setVisibility(View.GONE); r3.setVisibility(View.GONE);}

                            }
                            else
                            {
                                filter.setContentView(R.layout.variation_modal_qty);
                                p1 = filter.findViewById(R.id.p1);
                                p2 = filter.findViewById(R.id.p2);
                                p3 = filter.findViewById(R.id.p3);
                                r1 = filter.findViewById(R.id.q1);
                                r2 = filter.findViewById(R.id.q2);
                                r3 = filter.findViewById(R.id.q3);
                                if(!dif[1].equals("0"))
                                {   p1.append(dif[1]);}
                                else
                                {  p1.setVisibility(View.GONE); r1.setVisibility(View.GONE); }
                                if(!dif[2].equals("0"))
                                    p2.append(dif[2]);
                                else
                                {  p2.setVisibility(View.GONE); r2.setVisibility(View.GONE);}

                                if(!dif[3].equals("0"))
                                    p3.append(dif[3]);
                                else
                                {  p3.setVisibility(View.GONE); r3.setVisibility(View.GONE);}
                            }

                            final RadioGroup radioGroup;
                            TextView headtitle;
                            ImageView plusQ;
                            ImageView close;
                            ImageView minusQ;
                            final TextView upBox;
                            TextView AddVariation;
                            filter.setCanceledOnTouchOutside(false);

                            if(!filter.isShowing())
                            filter.show();

                            radioGroup = filter.findViewById(R.id.filterGroup);
                            headtitle = filter.findViewById(R.id.productName);
                            close = filter.findViewById(R.id.b_close);

                            plusQ = filter.findViewById(R.id.addQtyV);
                            minusQ = filter.findViewById(R.id.removeQtyV);
                            upBox = filter.findViewById(R.id.qtyUpdateV);
                            AddVariation = filter.findViewById(R.id.AddVariation);
                            //AddVariation.setText("Add \u20B9"+dif[1]);

                            headtitle.setText(title);

                            fam = Integer.parseInt(dif[1]);

                            AddVariation.setEnabled(false);
                            plusQ.setEnabled(false);
                            minusQ.setEnabled(false);


                            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(RadioGroup group, int checkedId) {

                                    RadioButton radio =  filter.findViewById(checkedId);
                                    String selVar = radio.getHint().toString();

                                    AddVariation.setEnabled(true);
                                    plusQ.setEnabled(true);
                                    minusQ.setEnabled(true);

                                    if(selVar.equals("1"))
                                    {
                                        fam = Integer.parseInt(dif[1]);
                                        AddVariation.setText("Add \u20B9"+dif[1]);
                                        AddVariation.setHint(String.valueOf(fam));
                                    }
                                    else if(selVar.equals("2"))
                                    { fam = Integer.parseInt(dif[2]); AddVariation.setText("Add \u20B9"+dif[2]);
                                        AddVariation.setHint(String.valueOf(fam));
                                    }
                                    else
                                    {  fam = Integer.parseInt(dif[3]); AddVariation.setText("Add \u20B9"+dif[3]);
                                        AddVariation.setHint(String.valueOf(fam));}

                                }
                            });

                            plusQ.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    int selectedId = radioGroup.getCheckedRadioButtonId();
                                    RadioButton radio =  filter.findViewById(selectedId);

                                    String selVar = radio.getHint().toString();

                                    if(selVar.equals("1"))
                                        fam = Integer.parseInt(dif[1]);
                                    else if(selVar.equals("2"))
                                        fam = Integer.parseInt(dif[2]);
                                    else
                                        fam = Integer.parseInt(dif[3]);

                                    int CurrentQty = Integer.parseInt(upBox.getText().toString());
                                    if(CurrentQty!=20)
                                    {
                                        CurrentQty += 1;
                                        upBox.setText(String.valueOf(CurrentQty));
                                        AddVariation.setText("Add \u20B9"+ fam * CurrentQty);
                                        AddVariation.setHint(String.valueOf(fam * CurrentQty));
                                    }
                                    else
                                    {
                                        Toast.makeText(context,"You can't Exceed Limit!",Toast.LENGTH_SHORT).show();
                                    }


                                }
                            });


                            minusQ.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    int selectedId = radioGroup.getCheckedRadioButtonId();
                                    RadioButton radio =  filter.findViewById(selectedId);

                                    String selVar = radio.getHint().toString();

                                    if(selVar.equals("1"))
                                        fam = Integer.parseInt(dif[1]);
                                    else if(selVar.equals("2"))
                                        fam = Integer.parseInt(dif[2]);
                                    else
                                        fam = Integer.parseInt(dif[3]);

                                    int CurrentQty = Integer.parseInt(upBox.getText().toString());

                                    if (CurrentQty == 1) {
                                        filter.dismiss();
                                    }
                                    else
                                    {
                                        CurrentQty -= 1;

                                        upBox.setText(String.valueOf(CurrentQty));
                                        AddVariation.setText("Add \u20B9"+ fam * CurrentQty);
                                        AddVariation.setHint(String.valueOf(fam * CurrentQty));
                                    }

                                }
                            });

                            close.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    filter.dismiss();
                                }
                            });

                            AddVariation.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                     AddtoCart(Id, ShopId, upBox.getText().toString(),fam+"");
                                    ((ModalSecond) holder).Box.setVisibility(View.VISIBLE);
                                    ((ModalSecond) holder).CartClick.setVisibility(View.GONE);
                                    ((ModalSecond) holder).price.setHint(String.valueOf(fam));
                                    ((ModalSecond) holder).priceUpdate.setText("\u20B9"+AddVariation.getHint().toString());
                                     checkBtn.setVisibility(View.VISIBLE);
                                    ((ModalSecond) holder).qtyUpdate.setText(upBox.getText().toString());
                                    filter.dismiss();
                                }
                            });
                        }

                    }
                });

                if(!modalOffersList.get(position).getVariation().equals("null"))
                {
                    ((ModalSecond) holder).customize.setVisibility(View.VISIBLE);
                }


                plus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int amount = Integer.parseInt(((ModalSecond) holder).price.getHint().toString());

                        plus.setEnabled(false);
                        int CurrentQty = Integer.parseInt(((ModalSecond) holder).qtyUpdate.getText().toString());

                        if (CurrentQty == 1) {
                            CurrentQty += 1;
                            ((ModalSecond) holder).qtyUpdate.setText(String.valueOf(CurrentQty));
                            ((ModalSecond) holder).priceUpdate.setText("\u20B9 " + amount * CurrentQty);
                        } else {
                            if(CurrentQty!=20)
                            {
                                CurrentQty += 1;
                                ((ModalSecond) holder).qtyUpdate.setText(String.valueOf(CurrentQty));
                                ((ModalSecond) holder).priceUpdate.setText("\u20B9 " + amount * CurrentQty);
                            }
                            else
                            {
                                Toast.makeText(context,"You can't Exceed Limit!",Toast.LENGTH_SHORT).show();
                            }
                        }

                        updateQtyCart(Id, CurrentQty);
                    }
                });

                minus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        int amount = Integer.parseInt(((ModalSecond) holder).price.getHint().toString());
                        minus.setEnabled(false);

                        int CurrentQty = Integer.parseInt(((ModalSecond) holder).qtyUpdate.getText().toString());
                        if (CurrentQty == 1) {

                            ((ModalSecond) holder).Box.setVisibility(View.GONE);
                            ((ModalSecond) holder).CartClick.setVisibility(View.VISIBLE);

                            updateQtyCart(Id, CurrentQty);

                            RemovetoCart(Id);

                        } else {
                            CurrentQty -= 1;
                            ((ModalSecond) holder).qtyUpdate.setText(String.valueOf(CurrentQty));
                            ((ModalSecond) holder).priceUpdate.setText("\u20B9 " + amount * CurrentQty);

                            updateQtyCart(Id, CurrentQty);
                        }

                    }
                });
                break;
        }


    }

    @Override
    public int getItemCount() {
        if (modalOffersList == null)
            return 0;
        else
            return  modalOffersList.size();
    }



    public static class ModalFirst extends RecyclerView.ViewHolder {

        TextView product, desc;

        ModalFirst(@NonNull View itemView) {
            super(itemView);

            product = itemView.findViewById(R.id.cat_title_res);
            desc = itemView.findViewById(R.id.cat_total_pro);
        }
    }

    public class ModalSecond extends RecyclerView.ViewHolder {

        ImageView proThumb;
        TextView product, price, desc, CartClick, qtyUpdate, priceUpdate, pro_discount,customize;
        LinearLayout Box;

         ModalSecond(@NonNull View itemView) {
            super(itemView);

            proThumb = itemView.findViewById(R.id.pro_thumb);
            CartClick = itemView.findViewById(R.id.addtoCart);
            product = itemView.findViewById(R.id.pro_title);
            price = itemView.findViewById(R.id.pro_price);
            desc = itemView.findViewById(R.id.pro_desc);
            pro_discount = itemView.findViewById(R.id.pro_discount);
            Box = itemView.findViewById(R.id.qtyBox);

            plus = itemView.findViewById(R.id.addQty);
             minus = itemView.findViewById(R.id.removeQty);
             customize = itemView.findViewById(R.id.customize);
            qtyUpdate = itemView.findViewById(R.id.qtyUpdate);
            priceUpdate = itemView.findViewById(R.id.priceUpdate);

        }

    }

    public void AddtoCart(String id, final String shop, String qty,String price) {

        final SharedPrefManager sharedPrefManager = new SharedPrefManager(context);
        Call<DefaultResponse> call = RetrofitClient.getInstance().getApi().AddtoCart(id, shop, qty, price,sharedPrefManager.getsUser().getUid());
        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                DefaultResponse dr = response.body();
                sharedPrefManager.setCart(Integer.parseInt(dr.getMessage()));
            }

            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {
            }
        });
    }

    public void updateQtyCart(String id, int qty) {
        final SharedPrefManager sharedPrefManager = new SharedPrefManager(context);

        Call<DefaultResponse> call = RetrofitClient.getInstance().getApi().updateQty(id, qty, sharedPrefManager.getsUser().getUid());
        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                DefaultResponse dr = response.body();

                if (response.code() == 201) {
                    if (call.isExecuted()) {
                        plus.setEnabled(true);
                        minus.setEnabled(true);
                    }
                } else {
                }
            }

            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {
            }
        });
    }

    public void RemovetoCart(String id) {

        final SharedPrefManager sharedPrefManager = new SharedPrefManager(context);
        sharedPrefManager.setCart(sharedPrefManager.getsUser().getItem() - 1);
        Call<DefaultResponse> call = RetrofitClient.getInstance().getApi().RemovefromCart(id, sharedPrefManager.getsUser().getUid());
        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                DefaultResponse dr = response.body();
                if (response.code() == 201) {
                    sharedPrefManager.setCart(Integer.parseInt(dr.getMessage()));
                    minus.setEnabled(true);
                }
            }

            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {
            }
        });
    }

    @Override
    public int getItemViewType(int position) {

        switch (modalOffersList.get(position).getProType())
        {
            case 1:
                return ModalOffers.TYPE_1;
            case 2:
                return ModalOffers.TYPE_2;
            default:
                return -1;
        }
    }


}