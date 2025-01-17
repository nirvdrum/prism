package org.prism;

import com.dylibso.chicory.runtime.HostImports;
import com.dylibso.chicory.runtime.Module;
import com.dylibso.chicory.wasm.types.Value;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DummyTest {

    @Test
    public void test1() {
        var wasmPrism = Module.build(new File("src/test/resources/prism.wasm")).instantiate();
        var memory = wasmPrism.getMemory();
        var calloc = wasmPrism.getExport("calloc");
        var pmSerializeParse = wasmPrism.getExport("pm_serialize_parse");
        var pmBufferInit = wasmPrism.getExport("pm_buffer_init");
        var pmBufferSizeof = wasmPrism.getExport("pm_buffer_sizeof");
        var pmBufferValue = wasmPrism.getExport("pm_buffer_value");
        var pmBufferLength = wasmPrism.getExport("pm_buffer_length");

        // The Ruby source code to be processed
        var source = "1 + 1";

        var sourcePointer = calloc.apply(Value.i32(1), Value.i32(source.length()));
        memory.writeString(sourcePointer[0].asInt(), source);

        var packedOptions = new byte[] {};
        var optionsPointer = calloc.apply(Value.i32(1), Value.i32(packedOptions.length));
        memory.write(optionsPointer[0].asInt(), packedOptions);

        var bufferPointer = calloc.apply(pmBufferSizeof.apply()[0], Value.i32(1));
        pmBufferInit.apply(bufferPointer);

        pmSerializeParse.apply(
                bufferPointer[0], sourcePointer[0], Value.i32(source.length()), optionsPointer[0]);

        var result = memory.readBytes(
                pmBufferValue.apply(bufferPointer[0])[0].asInt(),
                pmBufferLength.apply(bufferPointer[0])[0].asInt());

        System.out.println("RESULT: " + new String(result));

        var byteResults = result;
        Nodes.Source nodeSource = new Nodes.Source(byteResults);
        ParseResult pr = Loader.load(byteResults, nodeSource);

        assertEquals(1, pr.value.childNodes().length);
        System.out.println("Nodes:");
        System.out.println(pr.value.childNodes()[0]);
        assertTrue(pr.value.childNodes()[0].toString().contains("IntegerNode"));
    }

    @Test
    public void test2() {
        var wasmPrism = Module.build(new File("src/test/resources/prism.wasm")).instantiate();
        var memory = wasmPrism.getMemory();
        var calloc = wasmPrism.getExport("calloc");
        var pmSerializeParse = wasmPrism.getExport("pm_serialize_parse");
        var pmBufferInit = wasmPrism.getExport("pm_buffer_init");
        var pmBufferSizeof = wasmPrism.getExport("pm_buffer_sizeof");
        var pmBufferValue = wasmPrism.getExport("pm_buffer_value");
        var pmBufferLength = wasmPrism.getExport("pm_buffer_length");

        // The Ruby source code to be processed
        var source = "puts \"h\ne\nl\nl\no\n\"";

        var sourcePointer = calloc.apply(Value.i32(1), Value.i32(source.length()));
        memory.writeString(sourcePointer[0].asInt(), source);

        var packedOptions = new byte[] {};
        var optionsPointer = calloc.apply(Value.i32(1), Value.i32(packedOptions.length));
        memory.write(optionsPointer[0].asInt(), packedOptions);

        var bufferPointer = calloc.apply(pmBufferSizeof.apply()[0], Value.i32(1));
        pmBufferInit.apply(bufferPointer);

        pmSerializeParse.apply(
                bufferPointer[0], sourcePointer[0], Value.i32(source.length()), optionsPointer[0]);

        var result = memory.readBytes(
                pmBufferValue.apply(bufferPointer[0])[0].asInt(),
                pmBufferLength.apply(bufferPointer[0])[0].asInt());

        System.out.println("RESULT: " + new String(result));

        var byteResults = result;
        Nodes.Source nodeSource = new Nodes.Source(byteResults);
        ParseResult pr = Loader.load(byteResults, nodeSource);

        assertEquals(1, pr.value.childNodes().length);

        System.out.println("Nodes:");
        System.out.println(pr.value.childNodes()[0]);
        assertTrue(pr.value.childNodes()[0].toString().contains("CallNode"));
    }

}
