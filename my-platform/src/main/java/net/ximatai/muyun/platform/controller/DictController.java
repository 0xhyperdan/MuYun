package net.ximatai.muyun.platform.controller;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import net.ximatai.muyun.ability.IChildAbility;
import net.ximatai.muyun.ability.ITreeAbility;
import net.ximatai.muyun.base.BaseBusinessTable;
import net.ximatai.muyun.core.database.MyTableWrapper;
import net.ximatai.muyun.database.builder.Column;
import net.ximatai.muyun.database.builder.TableWrapper;
import net.ximatai.muyun.model.TreeNode;
import net.ximatai.muyun.platform.ScaffoldForPlatform;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

//@Path(BASE_PATH + "/dict")
@ApplicationScoped
public class DictController extends ScaffoldForPlatform implements ITreeAbility, IChildAbility {

    @Inject
    BaseBusinessTable base;

    @Override
    public String getMainTable() {
        return "app_dict";
    }

    @Override
    public TableWrapper getTableWrapper() {
        return new MyTableWrapper(this)
            .setPrimaryKey(Column.ID_POSTGRES)
            .setInherit(base.getTableWrapper())
            .addColumn("id_at_app_dictcategory")
            .addColumn("v_value")
            .addColumn("v_name")
            .addColumn("v_remark");
    }

    @Override
    public String create(Map body) {
        //这样就可以把id_at_app_dictcategory当作ROOT_ID使用了
        Objects.requireNonNull(body.get("id_at_app_dictcategory"));
        String treePidName = Column.TREE_PID.getName();
        HashMap map = new HashMap<>(body);
        if (map.get(treePidName) == null) {
            map.put(treePidName, body.get("id_at_app_dictcategory"));
        }
        return super.create(map);
    }

    @Override
    public List<TreeNode> tree(String rootID, Boolean showMe, String labelColumn, Integer maxLevel) {
        Objects.requireNonNull(rootID);
        if (showMe == null) {
            showMe = false;
        }
        return ITreeAbility.super.tree(rootID, showMe, labelColumn, maxLevel);
    }
}
