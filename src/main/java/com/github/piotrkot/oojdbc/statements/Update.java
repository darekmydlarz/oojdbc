/*
 * Copyright (c) 2012-2018, jcabi.com
 * Copyright (c) 2021, github.com/piotrkot
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met: 1) Redistributions of source code must retain the above
 * copyright notice, this list of conditions and the following
 * disclaimer. 2) Redistributions in binary form must reproduce the above
 * copyright notice, this list of conditions and the following
 * disclaimer in the documentation and/or other materials provided
 * with the distribution. 3) Neither the name of the jcabi.com nor
 * the names of its contributors may be used to endorse or promote
 * products derived from this software without specific prior written
 * permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL
 * THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.github.piotrkot.oojdbc.statements;

import com.github.piotrkot.oojdbc.Connect;
import com.github.piotrkot.oojdbc.Outcome;
import com.github.piotrkot.oojdbc.Request;
import com.github.piotrkot.oojdbc.Sql;
import com.github.piotrkot.oojdbc.Stmnt;
import java.sql.Connection;
import java.sql.PreparedStatement;
import lombok.RequiredArgsConstructor;

/**
 * JDBC update.
 * @param <T> Type of expected result
 * @since 1.0
 */
@RequiredArgsConstructor
public final class Update<T> implements Stmnt<T> {
    /**
     * SQL command.
     */
    private final Sql sql;

    /**
     * Parameters to SQL command.
     */
    private final Args args;

    /**
     * Outcome of ResultSet.
     */
    private final Outcome<T> outcome;

    /**
     * Ctor.
     * @param sql SQL command
     * @param outcome Outcome of ResultSet
     */
    public Update(final Sql sql, final Outcome<T> outcome) {
        this(sql, new Args(), outcome);
    }

    @Override
    public T using(final Connection conn) throws Exception {
        final PreparedStatement stmt = new Connect.WithKeys(this.sql.asString())
            .open(conn);
        this.args.prepare(stmt);
        return this.outcome.handle(
            Request.EXECUTE_UPDATE.fetch(stmt),
            stmt
        );
    }
}
