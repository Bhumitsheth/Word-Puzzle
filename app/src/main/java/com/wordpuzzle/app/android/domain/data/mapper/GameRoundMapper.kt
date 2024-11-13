package com.wordpuzzle.app.android.domain.data.mapper

import com.wordpuzzle.app.android.commons.Mapper;
import com.wordpuzzle.app.android.commons.generator.StringGridGenerator;
import com.wordpuzzle.app.android.domain.data.entity.GameRoundEntity;
import com.wordpuzzle.app.android.domain.model.GameRound;
import com.wordpuzzle.app.android.domain.model.Grid;

class GameRoundMapper : Mapper<GameRoundEntity?, GameRound?>() {
    override fun map(obj: GameRoundEntity?): GameRound? {
        if (obj == null) return null

        val gr = GameRound()
        gr.info = obj.info!!
        val grid = Grid(obj.gridRowCount, obj.gridColCount)
        gr.grid = grid

        if (obj.gridData != null && obj.gridData!!.isNotEmpty()) {
            StringGridGenerator().setGrid(obj.gridData!!, grid.array)
        }

        gr.addUsedWords(obj.usedWords)

        return gr
    }

    override fun revMap(obj: GameRound?): GameRoundEntity? {
        if (obj == null) return null

        val ent = GameRoundEntity()
        ent.info = obj.info

        if (obj.grid != null) {
            ent.gridRowCount = obj.grid!!.rowCount
            ent.gridColCount = obj.grid!!.colCount
            ent.gridData = obj.grid.toString()
        }

        ent.usedWords = obj.usedWords

        return ent
    }
}
