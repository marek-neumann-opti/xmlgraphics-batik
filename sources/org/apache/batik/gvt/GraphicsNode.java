/*****************************************************************************
 * Copyright (C) The Apache Software Foundation. All rights reserved.        *
 * ------------------------------------------------------------------------- *
 * This software is published under the terms of the Apache Software License *
 * version 1.1, a copy of which has been included with this distribution in  *
 * the LICENSE file.                                                         *
 *****************************************************************************/

package org.apache.batik.gvt;

import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.EventListener;
import java.util.Map;
import org.apache.batik.ext.awt.image.renderable.Clip;
import org.apache.batik.ext.awt.image.renderable.Filter;
import org.apache.batik.gvt.event.GraphicsNodeEvent;
import org.apache.batik.gvt.event.GraphicsNodeKeyEvent;
import org.apache.batik.gvt.event.GraphicsNodeKeyListener;
import org.apache.batik.gvt.event.GraphicsNodeMouseEvent;
import org.apache.batik.gvt.event.GraphicsNodeMouseListener;
import org.apache.batik.gvt.filter.Mask;

/**
 * The base class for all graphics nodes. A GraphicsNode encapsulates
 * graphical attributes and can perform atomic operations of a complex
 * rendering.
 *
 * @author <a href="mailto:Thierry.Kormann@sophia.inria.fr">Thierry Kormann</a>
 * @author <a href="mailto:etissandier@ilog.fr">Emmanuel Tissandier</a>
 * @version $Id$
 */
public interface GraphicsNode {

    //
    // Properties methods
    //

    /**
     * Sets the transform of this node.
     * @param newTransform the new transform of this node
     */
    void setTransform(AffineTransform newTransform);

    /**
     * Returns the transform of this node.
     */
    AffineTransform getTransform();

    /**
     * Returns the concatenated transform of this node. i.e., this
     * node's transform preconcatenated with it's parent's transforms.
     */
    AffineTransform getGlobalTransform();

    /**
     * Sets the composite of this node.
     * @param composite the composite of this node
     */
    void setComposite(Composite newComposite);

    /**
     * Returns the composite of this node.
     */
    Composite getComposite();

    /**
     * Sets if this node is visible or not depending on the specified value.
     * @param isVisible If true this node is visible
     */
    void setVisible(boolean isVisible);

    /**
     * Determines whether or not this node is visible when its parent
     * is visible. Nodes are initially visible.
     * @return true if this node is visible, false otherwise
     */
    boolean isVisible();

    /**
     * Sets the clipping filter for this node.
     * @param newClipper the new clipping filter of this node
     */
    void setClip(Clip newClipper);

    /**
     * Returns the clipping filter of this node or null if any.
     */
    Clip getClip();

    /**
     * Maps the specified key to the specified value in the rendering
     * hints of this node.
     * @param key the key of the hint to be set
     * @param value the value indicating preferences for the specified
     * hint category.
     */
    void setRenderingHint(RenderingHints.Key key, Object value);

    /**
     * Copies all of the mappings from the specified Map to the
     * rendering hints of this node.
     * @param hints the rendering hints to be set
     */
    void setRenderingHints(Map hints);

    /**
     * Sets the rendering hints of this node.
     * @param newHints the new rendering hints of this node
     */
    void setRenderingHints(RenderingHints newHints);

    /**
     * Returns the rendering hints of this node or null if any.
     */
    RenderingHints getRenderingHints();

    /**
     * Sets the mask of this node.
     * @param newMask the new mask of this node
     */
    void setMask(Mask newMask);

    /**
     * Returns the mask of this node or null if any.
     */
    Mask getMask();

    /**
     * Sets the filter of this node.
     * @param newFilter the new filter of this node
     */
    void setFilter(Filter newFilter);

    /**
     * Returns the filter of this node or null if any.
     */
    Filter getFilter();

    //
    // Drawing methods
    //

    /**
     * Paints this node.
     *
     * @param g2d the Graphics2D to use
     * @param rc the GraphicsNodeRenderContext to use
     * @exception InterruptedException thrown if the current thread
     * was interrupted during paint
     */
    void paint(Graphics2D g2d, GraphicsNodeRenderContext rc)
            throws InterruptedException;

    /**
     * Paints this node without applying Filter, Mask, Composite and clip.
     * @param g2d the Graphics2D to use
     * @param rc the GraphicsNodeRenderContext to use
     */
    void primitivePaint(Graphics2D g2d, GraphicsNodeRenderContext rc);

    //
    // Event support methods
    //

    /**
     * Dispatches the specified event to the interested registered listeners.
     * @param evt the event to dispatch
     */
    void dispatchEvent(GraphicsNodeEvent evt);

    /**
     * Adds the specified graphics node mouse listener to receive
     * graphics node mouse events from this node.
     * @param l the graphics node mouse listener to add
     */
    void addGraphicsNodeMouseListener(GraphicsNodeMouseListener l);

    /**
     * Removes the specified graphics node mouse listener so that it
     * no longer receives graphics node mouse events from this node.
     * @param l the graphics node mouse listener to remove
     */
    void removeGraphicsNodeMouseListener(GraphicsNodeMouseListener l);

    /**
     * Adds the specified graphics node key listener to receive
     * graphics node key events from this node.
     * @param l the graphics node key listener to add
     */
    void addGraphicsNodeKeyListener(GraphicsNodeKeyListener l);

    /**
     * Removes the specified graphics node key listener so that it
     * no longer receives graphics node key events from this node.
     * @param l the graphics node key listener to remove
     */
    void removeGraphicsNodeKeyListener(GraphicsNodeKeyListener l);

    /**
     * Sets the hit detector for this node.
     * @param hitDetector the new hit detector
     */
    void setGraphicsNodeHitDetector(GraphicsNodeHitDetector hitDetector);

    /**
     * Returns the hit detector for this node.
     */
    GraphicsNodeHitDetector getGraphicsNodeHitDetector();

    /**
     * Dispatches a graphics node mouse event to this node or one of its child.
     * @param evt the evt to dispatch
     */
    void processMouseEvent(GraphicsNodeMouseEvent evt);

    /**
     * Dispatches a graphics node key event to this node or one of its child.
     * @param evt the evt to dispatch
     */
    void processKeyEvent(GraphicsNodeKeyEvent evt);

    /**
     * Returns an array of listeners that were added to this node and
     * of the specified type.
     * @param listenerType the type of the listeners to return
     */
    EventListener [] getListeners(Class listenerType);

    //
    // Structural methods
    //

    /**
     * Returns the parent of this node or null if any.
     */
    CompositeGraphicsNode getParent();

    /**
     * Returns the root of the GVT tree or <code>null</code> if
     * the node is not part of a GVT tree.
     */
    RootGraphicsNode getRoot();

    //
    // Geometric methods
    //

    /**
     * Returns the bounds of this node in user space. This includes
     * primitive paint, filtering, clipping and masking.
     * <b>Note</b>: The boundaries of some nodes (notably, text element nodes)
     * cannot be precisely determined independent of their
     * GraphicsNodeRenderContext.
     *
     * @param rc the GraphicsNodeRenderContext for which this dimension applies
     */
    Rectangle2D getBounds(GraphicsNodeRenderContext rc);

    /**
     * Returns the bounds of the area covered by this node's
     * primitive paint.
     * <b>Note</b>: The boundaries of some nodes (notably, text element nodes)
     * cannot be precisely determined independent of their
     * GraphicsNodeRenderContext.
     *
     * @param rc the GraphicsNodeRenderContext for which this dimension applies
     */
    Rectangle2D getPrimitiveBounds(GraphicsNodeRenderContext rc);

    /**
     * Returns the bounds of the area covered by this node, without
     * taking any of its rendering attribute into account, i.e., exclusive
     * of any clipping, masking, filtering or stroking, for example.
     * <b>Note</b>: The boundaries of some nodes (notably, text element nodes)
     * cannot be precisely determined independent of their
     * GraphicsNodeRenderContext.
     *
     * @param rc the GraphicsNodeRenderContext for which this dimension applies
     */
    Rectangle2D getGeometryBounds(GraphicsNodeRenderContext rc);

    /**
     * Tests if the specified Point2D is inside the boundary of this node.
     * <b>Note</b>: The boundaries of some nodes (notably, text element nodes)
     * cannot be precisely determined independent of their
     * GraphicsNodeRenderContext.
     *
     * @param p the specified Point2D in the user space
     * @param rc the GraphicsNodeRenderContext for which this dimension applies
     * @return true if the coordinates are inside, false otherwise
     */
    boolean contains(Point2D p, GraphicsNodeRenderContext rc);

    /**
     * Tests if the interior of this node intersects the interior of a
     * specified Rectangle2D.
     * <b>Note</b>: The boundaries of some nodes (notably, text element nodes)
     * cannot be precisely determined independent of their
     * GraphicsNodeRenderContext.
     *
     * @param r the specified Rectangle2D in the user node space
     * @param rc the GraphicsNodeRenderContext for which this dimension applies
     * @return true if the rectangle intersects, false otherwise
     */
    boolean intersects(Rectangle2D r, GraphicsNodeRenderContext rc);

    /**
     * Returns the GraphicsNode containing point p if this node or one of
     * its children is sensitive to mouse events at p.
     * <b>Note</b>: The boundaries of some nodes (notably, text element nodes)
     * cannot be precisely determined independent of their
     * GraphicsNodeRenderContext.
     *
     * @param p the specified Point2D in the user space
     * @param rc the GraphicsNodeRenderContext for which this dimension applies
     * @return the GraphicsNode containing p on this branch of the GVT tree.
     */
    GraphicsNode nodeHitAt(Point2D p, GraphicsNodeRenderContext rc);

    /**
     * Returns the outline of this node.
     * <b>Note</b>: The boundaries of some nodes (notably, text element nodes)
     * cannot be precisely determined independent of their
     * GraphicsNodeRenderContext.
     *
     * @param rc the GraphicsNodeRenderContext for which this dimension applies
     * @return the outline of this node
     */
    Shape getOutline(GraphicsNodeRenderContext rc);
}
