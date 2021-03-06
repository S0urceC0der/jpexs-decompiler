/*
 *  Copyright (C) 2010-2015 JPEXS, All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library.
 */
package com.jpexs.decompiler.flash.tags;

import com.jpexs.decompiler.flash.SWF;
import com.jpexs.decompiler.flash.SWFInputStream;
import com.jpexs.decompiler.flash.SWFOutputStream;
import com.jpexs.decompiler.flash.types.BasicType;
import com.jpexs.decompiler.flash.types.annotations.SWFArray;
import com.jpexs.decompiler.flash.types.annotations.SWFType;
import com.jpexs.helpers.ByteArrayRange;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Makes portions of a SWF file available for import by other SWF files
 *
 * @author JPEXS
 */
public class ExportAssetsTag extends Tag {

    /**
     * HashMap with assets
     */
    @SWFType(value = BasicType.UI16)
    @SWFArray(value = "tag", countField = "count")
    public List<Integer> tags;

    @SWFArray(value = "name", countField = "count")
    public List<String> names;

    public static final int ID = 56;

    /**
     * Constructor
     *
     * @param swf
     */
    public ExportAssetsTag(SWF swf) {
        super(swf, ID, "ExportAssets", null);
        tags = new ArrayList<>();
        names = new ArrayList<>();
    }

    /**
     * Constructor
     *
     * @param sis
     * @param data
     * @throws IOException
     */
    public ExportAssetsTag(SWFInputStream sis, ByteArrayRange data) throws IOException {
        super(sis.getSwf(), ID, "ExportAssets", data);
        readData(sis, data, 0, false, false, false);
    }

    @Override
    public final void readData(SWFInputStream sis, ByteArrayRange data, int level, boolean parallel, boolean skipUnusualTags, boolean lazy) throws IOException {
        int count = sis.readUI16("count");
        tags = new ArrayList<>();
        names = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            int characterId = sis.readUI16("characterId");
            tags.add(characterId);
            String name = sis.readString("name");
            names.add(name);
        }
    }

    /**
     * Gets data bytes
     *
     * @return Bytes of data
     */
    @Override
    public byte[] getData() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OutputStream os = baos;
        SWFOutputStream sos = new SWFOutputStream(os, getVersion());
        try {
            sos.writeUI16(tags.size());
            for (int i = 0; i < tags.size(); i++) {
                sos.writeUI16(tags.get(i));
                sos.writeString(names.get(i));
            }
        } catch (IOException e) {
            throw new Error("This should never happen.", e);
        }
        return baos.toByteArray();
    }
}
