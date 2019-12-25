/* Copyright 2019 The TensorFlow Authors. All Rights Reserved.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
==============================================================================*/

package freewill.rakesh.com.howdoyoufeel.tflite;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.os.SystemClock;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

import org.tensorflow.lite.Interpreter;


/** A classifier specialized to label images using TensorFlow Lite. */
public class MoodClassifier {

    private float[][] labelProbArray = null;


    /** The runtime device type used for executing classification. */
    public enum Device {
        CPU,
        NNAPI,
        GPU
    }

    /** Number of results to show in the UI. */
    private static final int MAX_RESULTS = 3;




    /** Options for configuring the Interpreter. */
    private final Interpreter.Options tfliteOptions = new Interpreter.Options();

    /** The loaded TensorFlow Lite model. */
    private MappedByteBuffer tfliteModel;

    /** Labels corresponding to the output of the vision model. */
    private List<String> labels;


    /** An instance of the driver class to run model inference with Tensorflow Lite. */
    protected Interpreter tflite;

    /** A ByteBuffer to hold image data, to be feed into Tensorflow Lite as inputs. */
    protected ByteBuffer imgData = null;



    /** Initializes a {@code Classifier}. */
    public MoodClassifier(Activity activity, Device device, int numThreads) throws IOException {
        tfliteModel = loadModelFile(activity);
        switch (device) {
            case NNAPI:
                tfliteOptions.setUseNNAPI(true);
                break;
            case CPU:
                break;
        }
        tfliteOptions.setNumThreads(numThreads);
        tflite = new Interpreter(tfliteModel, tfliteOptions);
        labels = loadLabelList(activity);

    }

    /** Reads label list from Assets. */
    private List<String> loadLabelList(Activity activity) throws IOException {
        List<String> labels = new ArrayList<String>();
        BufferedReader reader =
                new BufferedReader(new InputStreamReader(activity.getAssets().open(getLabelPath())));
        String line;
        while ((line = reader.readLine()) != null) {
            labels.add(line);
        }
        reader.close();
        return labels;
    }

    /** Memory-map the model file in Assets. */
    private MappedByteBuffer loadModelFile(Activity activity) throws IOException {
        AssetFileDescriptor fileDescriptor = activity.getAssets().openFd(getModelPath());
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }

    /** Writes Image data into a {@code ByteBuffer}. */


    /** Runs inference and returns the classification results. */
    public List<Prediction> recognizeImage(final Bitmap bitmap) {
        runInference();

        // Find the best classifications.
        PriorityQueue<Prediction> pq =
                new PriorityQueue<Prediction>(
                        3,
                        new Comparator<Prediction>() {
                            @Override
                            public int compare(Prediction lhs, Prediction rhs) {
                                // Intentionally reversed to put high confidence at the head of the queue.
                                return Float.compare(rhs.getConfidence(), lhs.getConfidence());
                            }
                        });
        for (int i = 0; i < labels.size(); ++i) {
            pq.add(
                    new Prediction(
                            labels.size() > i ? labels.get(i) : "unknown",
                            getNormalizedProbability(i)
                            ));
        }
        final ArrayList<Prediction> recognitions = new ArrayList<Prediction>();
        int recognitionsSize = Math.min(pq.size(), MAX_RESULTS);
        for (int i = 0; i < recognitionsSize; ++i) {
            recognitions.add(pq.poll());
        }
        return recognitions;
    }

    /** Closes the interpreter and model to release resources. */
    public void close() {
        if (tflite != null) {
            tflite.close();
            tflite = null;
        }
        tfliteModel = null;
    }

    /**
     * Get the name of the model file stored in Assets.
     *
     * @return
     */
    protected String getModelPath()
    {
        return "mobilenet_v1_1.0_224.tflite";
    }

    /**
     * Get the name of the label file stored in Assets.
     *
     * @return
     */
    protected  String getLabelPath()
    {
        return "labels.txt";
    }




    /**
     * Read the probability value for the specified label This is either the original value as it was
     * read from the net's output or the updated value after the filter was applied.
     *
     * @param labelIndex
     * @return
     */
    protected  float getProbability(int labelIndex)
    {
        return labelProbArray[0][labelIndex];
    }


    protected void setProbability(int labelIndex, Number value) {
        labelProbArray[0][labelIndex] = value.floatValue();
    }


    protected float getNormalizedProbability(int labelIndex) {
        return labelProbArray[0][labelIndex];
    }


    protected void runInference() {
        tflite.run(imgData, labelProbArray);
    }

    /**
     * Get the total number of labels.
     *
     * @return
     */
    protected int getNumLabels() {
        return labels.size();
    }
}
