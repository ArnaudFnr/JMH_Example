/*
 * Copyright (c) 2014, Oracle America, Inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 *  * Neither the name of Oracle nor the names of its contributors may be used
 *    to endorse or promote products derived from this software without
 *    specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.sample;

import org.openjdk.jmh.annotations.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Fork(value = 2)
@BenchmarkMode({Mode.Throughput, Mode.AverageTime})
@Warmup(iterations = 5, time = 100,
        timeUnit = TimeUnit.MILLISECONDS)
@Measurement(iterations = 5, time = 100,
        timeUnit = TimeUnit.MILLISECONDS)
@Threads(2)
public class MyBenchmark {

    @Benchmark
    public double testMethod1(MyState state) {
        return state.value * Math.pow(2, state.power);
    }

    @Benchmark
    public double testMethod2(MyState state) {
        return state.value << state.power;
    }

    @State(Scope.Benchmark)
    public static class MyState {

        @Param({"5", "25"})
        int power;

        private int value;

        private int cursor;

        private List<Integer> values;

        @Setup(Level.Trial)
        public void beforeBenchmark() {
            values = new ArrayList<>(100000);
            for (int i = 0; i < 100000; i++) {
                values.add(i);
            }
        }

        @Setup(Level.Iteration)
        public void beforeIteration() {
            cursor = 0;
        }

        @Setup(Level.Invocation)
        public void beforeInvocation() {
            value = values.get(cursor++);
        }

        @TearDown(Level.Invocation)
        public void checkCursor() {
            if (cursor >= values.size()) {
                System.out.println("Cursor reinitialized.");
                cursor = 0;
            }
        }
    }

}


