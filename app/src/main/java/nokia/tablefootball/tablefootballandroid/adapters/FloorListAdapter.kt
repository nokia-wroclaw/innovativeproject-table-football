package nokia.tablefootball.tablefootballandroid.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.TextView

import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent

import nokia.tablefootball.tablefootballandroid.R
import nokia.tablefootball.tablefootballandroid.model.TableModel
import nokia.tablefootball.tablefootballandroid.utils.TableDataUtil

class FloorListAdapter(private val context: Context, tableModels: List<TableModel>) : BaseExpandableListAdapter()
{

    private var tablesMap: java.util.TreeMap<Int, ArrayList<TableModel>> = TableDataUtil.toFloorMap(tableModels)

    private var expandableListTitle: List<Int> = tablesMap.keys.toList()

    override fun getChild(
        listPosition: Int, expandedListPosition: Int): Any
    {
        return tablesMap[listPosition]!![expandedListPosition]
    }

    override fun getChildId(
        listPosition: Int, expandedListPosition: Int): Long
    {
        return expandedListPosition.toLong()
    }

    override fun getChildView(
        listPosition: Int, expandedListPosition: Int, isLastChild: Boolean,
        convertView: View?, parent: ViewGroup): View
    {
        var convertView = convertView
        val childHolder: ChildHolder

        if (convertView == null)
        {
            val layoutInflater = this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = layoutInflater.inflate(R.layout.item_group, parent, false)
            childHolder = ChildHolder()
            convertView!!.tag = childHolder
        }
        else
        {
            childHolder = convertView.tag as ChildHolder
        }

        childHolder.listView = convertView.findViewById(R.id.item_group_recycler_view) as RecyclerView

        val layoutManager = FlexboxLayoutManager(context).apply {
            flexWrap = FlexWrap.WRAP
            flexDirection = FlexDirection.ROW
            justifyContent = JustifyContent.CENTER
        }

        childHolder.listView!!.layoutManager = layoutManager
        childHolder.listView!!.adapter = ChildItemAdapter(context, tablesMap[expandableListTitle[listPosition]]!!)

        return convertView
    }

    override fun getChildrenCount(listPosition: Int): Int
    {
        return 1
    }

    override fun getGroup(listPosition: Int): Any
    {
        return this.expandableListTitle[listPosition]
    }

    override fun getGroupCount(): Int
    {
        return this.expandableListTitle.size
    }

    override fun getGroupId(listPosition: Int): Long
    {
        return listPosition.toLong()
    }

    override fun getGroupView(
        listPosition: Int, isExpanded: Boolean,
        convertView: View?, parent: ViewGroup): View
    {
        var convertView = convertView
        val listTitle = getGroup(listPosition) //as String
        if (convertView == null)
        {
            val layoutInflater = this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = layoutInflater.inflate(R.layout.floor_list, null)
        }
        val listTitleTextView = convertView!!
            .findViewById<View>(R.id.listTitle) as TextView

        listTitleTextView.text = "Floor ${listTitle}"
        return convertView
    }

    override fun hasStableIds(): Boolean
    {
        return false
    }

    override fun isChildSelectable(
        listPosition: Int, expandedListPosition: Int): Boolean
    {
        return true
    }

    private class ChildHolder
    {
        var listView: RecyclerView? = null
    }

    fun updateData(tableModels: List<TableModel>){
        tablesMap = TableDataUtil.toFloorMap(tableModels)
        expandableListTitle = tablesMap.keys.toList()

        notifyDataSetChanged()
    }

}
