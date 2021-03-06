/*
 * Twidere - Twitter client for Android
 *
 *  Copyright (C) 2012-2015 Mariotaku Lee <mariotaku.lee@gmail.com>
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.mariotaku.twidere.activity.support;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.WindowManager.LayoutParams;

import org.mariotaku.twidere.R;
import org.mariotaku.twidere.util.ThemeUtils;
import org.mariotaku.twidere.util.support.ViewSupport;
import org.mariotaku.twidere.view.iface.TintedStatusLayout;

/**
 * Created by mariotaku on 15/4/17.
 */
public class BaseDialogWhenLargeActivity extends BaseAppCompatActivity {

    private TintedStatusLayout mMainContent;

    @Override
    public int getThemeResourceId() {
        return ThemeUtils.getDialogWhenLargeThemeResource(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setupWindow();
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        final boolean result = super.onPrepareOptionsMenu(menu);
        if (!shouldSetActionItemColor()) return result;
        final Toolbar toolbar = peekActionBarToolbar();
        if (toolbar != null) {
            final int themeColor = getCurrentThemeColor();
            final int themeId = getCurrentThemeResourceId();
            final int itemColor = ThemeUtils.getContrastActionBarItemColor(this, themeId, themeColor);
            ThemeUtils.setActionBarOverflowColor(toolbar, itemColor);
            ThemeUtils.wrapToolbarMenuIcon(ViewSupport.findViewByType(toolbar, ActionMenuView.class), itemColor, itemColor);
        }
        return result;
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        mMainContent = (TintedStatusLayout) findViewById(R.id.main_content);
        setupTintStatusBar();
    }

    protected TintedStatusLayout getMainContent() {
        return mMainContent;
    }

    protected boolean isActionBarOutlineEnabled() {
        return true;
    }

    protected boolean shouldSetActionItemColor() {
        return true;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setupActionBar();
    }

    private void setupActionBar() {
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar == null) return;

        final int themeColor = getCurrentThemeColor();
        final int themeId = getCurrentThemeResourceId();
        final String option = getThemeBackgroundOption();
        final int titleColor = ThemeUtils.getContrastActionBarTitleColor(this, themeId, themeColor);
        final int actionBarItemsColor = ThemeUtils.getContrastActionBarItemColor(this, themeId, themeColor);
        ThemeUtils.applyActionBarBackground(actionBar, this, themeId, themeColor, option, isActionBarOutlineEnabled());
        final Toolbar toolbar = peekActionBarToolbar();
        if (toolbar != null) {
            ThemeUtils.setToolBarColor(toolbar, titleColor, actionBarItemsColor);
        } else {
            ThemeUtils.setActionBarColor(getWindow(), getSupportActionBar(), titleColor, actionBarItemsColor);
        }
    }

    private void setupTintStatusBar() {
        if (mMainContent == null) return;

        final int color = getCurrentThemeColor();
        final int alpha = ThemeUtils.isTransparentBackground(getThemeBackgroundOption()) ? getCurrentThemeBackgroundAlpha() : 0xFF;
        if (ThemeUtils.isDarkTheme(getCurrentThemeResourceId())) {
            mMainContent.setColor(getResources().getColor(R.color.background_color_action_bar_dark), alpha);
        } else {
            mMainContent.setColor(color, alpha);
        }

        mMainContent.setDrawShadow(false);
        mMainContent.setDrawColor(true);
        mMainContent.setFactor(1);
    }

    private void setupWindow() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }
}
