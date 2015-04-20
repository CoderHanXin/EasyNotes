/*
 * Copyright (c) 2015 Coder.HanXin
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.addict.easynotes.views.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.addict.easynotes.R;
import com.addict.easynotes.models.Note;
import com.addict.easynotes.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

public class MyStickyListAdapter extends BaseAdapter implements SectionIndexer, StickyListHeadersAdapter {

    private List<Note> mList;
    private int[] mSectionIndices;
    private String[] mSectionHeaders;
    private LayoutInflater mInflater;

    public MyStickyListAdapter(Context context, List<Note> list) {
        mInflater = LayoutInflater.from(context);
        mList = list;
        mSectionIndices = getSectionIndices();
        mSectionHeaders = getSectionHeaders();
    }

    public void refresh(List<Note> list) {
        mList = list;
        mSectionIndices = getSectionIndices();
        mSectionHeaders = getSectionHeaders();
        notifyDataSetChanged();
    }

    private int[] getSectionIndices() {
        if (mList.size() <= 0) {
            return new int[0];
        }
        List<Integer> sectionIndices = new ArrayList<>();
        String lastDate = DateUtils.dateOrTimeFormat(mList.get(0).getUpdateTime());
        sectionIndices.add(0);
        for (int i = 1; i < mList.size(); i++) {
            String date = DateUtils.dateOrTimeFormat(mList.get(i).getUpdateTime());
            if (!date.equals(lastDate)) {
                lastDate = date;
                sectionIndices.add(i);
            }
        }
        int[] sections = new int[sectionIndices.size()];
        for (int i = 0; i < sectionIndices.size(); i++) {
            sections[i] = sectionIndices.get(i);
        }
        return sections;
    }

    private String[] getSectionHeaders() {
        if (mList.size() <= 0) {
            return new String[0];
        }
        String[] sectionHeaders = new String[mSectionIndices.length];
        for (int i = 0; i < mSectionIndices.length; i++) {
            sectionHeaders[i] = DateUtils.dateOrTimeFormat(mList.get(mSectionIndices[i]).getUpdateTime());
        }
        return sectionHeaders;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mList.get(position).getNoteId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.list_item_today, parent, false);
            holder.tvDate = (TextView) convertView.findViewById(R.id.textView_date);
            holder.tvContent = (TextView) convertView.findViewById(R.id.textView_content);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvDate.setText(DateUtils.formatDateToString(mList.get(position).getUpdateTime(), "HH:mm:ss"));
        holder.tvContent.setText(mList.get(position).getContent());

        return convertView;
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        HeaderViewHolder holder;

        if (convertView == null) {
            holder = new HeaderViewHolder();
            convertView = mInflater.inflate(R.layout.list_header_history, parent, false);
            holder.tvHeader = (TextView) convertView.findViewById(R.id.textView_header);
            convertView.setTag(holder);
        } else {
            holder = (HeaderViewHolder) convertView.getTag();
        }

        holder.tvHeader.setText(DateUtils.dateOrTimeFormat(mList.get(position).getUpdateTime()));

        return convertView;
    }

    @Override
    public long getHeaderId(int position) {
        return DateUtils.getDateWithoutTime(mList.get(position).getUpdateTime()).getTime();
    }

    @Override
    public int getPositionForSection(int section) {
        if (mSectionIndices.length == 0) {
            return 0;
        }
        if (section >= mSectionIndices.length) {
            section = mSectionIndices.length - 1;
        } else if (section < 0) {
            section = 0;
        }
        return mSectionIndices[section];
    }

    @Override
    public int getSectionForPosition(int position) {
        for (int i = 0; i < mSectionIndices.length; i++) {
            if (position < mSectionIndices[i]) {
                return i - 1;
            }
        }
        return mSectionIndices.length - 1;
    }

    @Override
    public Object[] getSections() {
        return mSectionHeaders;
    }

    class ViewHolder {
        TextView tvDate;
        TextView tvContent;
    }

    class HeaderViewHolder {
        TextView tvHeader;
    }
}
