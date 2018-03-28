package com.myapp.rishabhrawat.cryptocurrencytracker.Adapter

import android.app.Activity
import android.graphics.Color
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.myapp.rishabhrawat.cryptocurrencytracker.Common.Common
import com.myapp.rishabhrawat.cryptocurrencytracker.Interface.ILoadMore
import com.myapp.rishabhrawat.cryptocurrencytracker.Model.CoinModel
import com.myapp.rishabhrawat.cryptocurrencytracker.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.coin_layout.view.*
import kotlin.math.sign

class CoinViewHolder(itemView:View): RecyclerView.ViewHolder(itemView){

    //this is card view layout item
    var coinIcon=itemView.coinIcon
    var coinsymbol=itemView.coin_symbol
    var coinname=itemView.coin_name
    var coinPrice=itemView.price_usd
    var oneHourChange=itemView.one_hour
    var twentyFourChange=itemView.twenty_four_hour
    var sevenDayChange=itemView.seven_day

}

class CoinAdapter(recyclerView: RecyclerView,internal var activity: Activity,var items:List<CoinModel>): RecyclerView.Adapter<CoinViewHolder>()
{

    internal var loadMore:ILoadMore?=null
    var isLoading:Boolean=false
    var visibleThreshold=5
    var lastVisibleItem:Int=0
    var totalItemCount:Int=0

    init {
        val linearLayout=recyclerView.layoutManager as LinearLayoutManager
        recyclerView.addOnScrollListener(object :RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                totalItemCount=linearLayout.itemCount
                lastVisibleItem=linearLayout.findLastVisibleItemPosition()
                if(!isLoading && totalItemCount<=lastVisibleItem+visibleThreshold )
                {
                    if(loadMore!=null)
                        loadMore!!.onLoadMore()
                    isLoading=true
                }
            }
        })
    }

    fun setLoadMore(loadMore: ILoadMore)
    {
        this.loadMore=loadMore
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinViewHolder {
        val view=LayoutInflater.from(activity).inflate(R.layout.coin_layout,parent,false)
        return CoinViewHolder(view)

    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: CoinViewHolder, position: Int) {
        val coinModel=items.get(position)
        val item=holder as CoinViewHolder

        item.coinname.text=coinModel.name
        item.coinsymbol.text=coinModel.symbol
        item.coinPrice.text=coinModel.price_usd
        item.oneHourChange.text=coinModel.percent_change_1h+"%"
        item.twentyFourChange.text=coinModel.percentage_change_24h+"%"
        item.sevenDayChange.text=coinModel.percentge_change_7d+"%"

        Picasso.with(activity.baseContext)
                .load(StringBuilder(Common.imageurl).append(coinModel.symbol!!.toLowerCase())
                        .append(".png")
                        .toString()).into(item.coinIcon)

        item.oneHourChange
                .setTextColor(if(coinModel.percent_change_1h!!.contains("-"))
                    Color.parseColor("#FF0000")
                else
                    Color.parseColor("#32CD32")
                )

//        item.twentyFourChange
//                .setTextColor(if(coinModel.percentage_change_24h!!.contains("-"))
//                    Color.parseColor("#FF0000")
//                else
//                    Color.parseColor("#32CD32")
//                )
//
//        item.sevenDayChange
//                .setTextColor(if(coinModel.percentge_change_7d!!.contains("-"))
//                    Color.parseColor("#FF0000")
//                else
//                    Color.parseColor("#32CD32")
//                )
    }

    fun setLoaded()
    {
        isLoading=false
    }

    fun updateData(coinModel:List<CoinModel>)
    {
        this.items=coinModel
        notifyDataSetChanged()
    }

}